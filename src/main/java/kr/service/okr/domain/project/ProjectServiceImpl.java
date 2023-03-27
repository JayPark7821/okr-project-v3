package kr.service.okr.domain.project;

import static kr.service.okr.domain.project.validator.ProjectValidatorType.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.common.exception.ErrorCode;
import kr.service.okr.common.exception.OkrApplicationException;
import kr.service.okr.domain.project.aggregate.feedback.Feedback;
import kr.service.okr.domain.project.aggregate.feedback.FeedbackRepository;
import kr.service.okr.domain.project.aggregate.feedback.SearchRange;
import kr.service.okr.domain.project.aggregate.initiative.Initiative;
import kr.service.okr.domain.project.aggregate.initiative.InitiativeRepository;
import kr.service.okr.domain.project.aggregate.team.TeamMember;
import kr.service.okr.domain.project.command.FeedbackSaveCommand;
import kr.service.okr.domain.project.command.ProjectDetailRetrieveCommand;
import kr.service.okr.domain.project.command.ProjectInitiativeSaveCommand;
import kr.service.okr.domain.project.command.ProjectKeyResultSaveCommand;
import kr.service.okr.domain.project.command.ProjectSaveCommand;
import kr.service.okr.domain.project.info.FeedbackDetailInfo;
import kr.service.okr.domain.project.info.FeedbackInfo;
import kr.service.okr.domain.project.info.IniFeedbackInfo;
import kr.service.okr.domain.project.info.InitiativeDetailInfo;
import kr.service.okr.domain.project.info.InitiativeDoneInfo;
import kr.service.okr.domain.project.info.InitiativeForCalendarInfo;
import kr.service.okr.domain.project.info.InitiativeInfo;
import kr.service.okr.domain.project.info.InitiativeSavedInfo;
import kr.service.okr.domain.project.info.ParticipateProjectInfo;
import kr.service.okr.domain.project.info.ProjectDetailInfo;
import kr.service.okr.domain.project.info.ProjectInfo;
import kr.service.okr.domain.project.info.ProjectSideMenuInfo;
import kr.service.okr.domain.project.info.ProjectTeamMembersInfo;
import kr.service.okr.domain.project.validator.ProjectValidateProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;
	private final InitiativeRepository initiativeRepository;
	private final FeedbackRepository feedbackRepository;
	private final ProjectValidateProcessor validateProcessor;
	private final ProjectAsyncService projectAsyncService;

	@Override
	public ProjectInfo registerProject(ProjectSaveCommand command, Long userSeq, List<Long> teamMemberUserSeqs) {
		Project project = projectRepository.save(command.toEntity());
		project.addLeader(userSeq);
		teamMemberUserSeqs.forEach(project::addTeamMember);
		return new ProjectInfo(project);
	}

	@Override
	public ProjectInfo getProjectInfoBy(String projectToken, Long userSeq) {
		return projectRepository.findByProjectTokenAndUser(projectToken, userSeq)
			.map(ProjectInfo::new)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN));
	}

	@Override
	public ProjectTeamMembersInfo inviteTeamMember(String projectToken, Long invitedUserSeq, Long inviterSeq) {
		Project project = inviteUserValidator(projectToken, invitedUserSeq, inviterSeq);
		project.addTeamMember(invitedUserSeq);
		return new ProjectTeamMembersInfo(
			project.getTeamMember().stream()
				.map(TeamMember::getUserSeq)
				.toList(),
			project.getObjective());
	}

	@Override
	public void validateUserToInvite(String projectToken, Long invitedUserSeq, Long inviterSeq) {
		inviteUserValidator(projectToken, invitedUserSeq, inviterSeq);
	}

	@Override
	public Page<ProjectDetailInfo> getDetailProjectList(ProjectDetailRetrieveCommand command, Long userSeq) {
		return projectRepository.getDetailProjectList(command, userSeq)
			.map(project -> new ProjectDetailInfo(project, userSeq));
	}

	@Override
	public ProjectSideMenuInfo getProjectSideMenuDetails(String projectToken, Long userSeq) {
		return projectRepository.findProgressAndTeamMembersByProjectTokenAndUser(projectToken, userSeq)
			.map(ProjectSideMenuInfo::new)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN));
	}

	@Override
	public String registerKeyResult(ProjectKeyResultSaveCommand command, Long userSeq) {
		Project project =
			projectRepository.findProjectKeyResultByProjectTokenAndUser(command.projectToken(), userSeq)
				.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN));

		validateProcessor.validate(
			List.of(VALIDATE_LEADER, VALIDATE_PROJECT_PERIOD, VALIDATE_KEYRESULT_COUNT),
			project,
			userSeq
		);

		return project.addKeyResult(command.keyResultName());
	}

	@Override
	public InitiativeSavedInfo registerInitiative(ProjectInitiativeSaveCommand command, Long userSeq) {

		Project project = projectRepository.findByKeyResultTokenAndUser(command.keyResultToken(), userSeq)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_KEYRESULT_TOKEN));

		Initiative initiative = buildInitiative(command, getTeamMember(userSeq, project));

		validateProcessor.validate(
			List.of(VALIDATE_PROJECT_PERIOD, VALIDATE_PROJECT_INITIATIVE_DATE),
			project,
			initiative
		);

		addInitiative(command, project, initiative);

		return new InitiativeSavedInfo(initiative.getInitiativeToken(), project.getId());
	}

	@Override
	public InitiativeDoneInfo initiativeFinished(String initiativeToken, Long userSeq) {
		Initiative initiative =
			initiativeRepository.findInitiativeByInitiativeTokenAndUserSeq(initiativeToken, userSeq)
				.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_INITIATIVE_TOKEN));

		validateProcessor.validate(
			List.of(VALIDATE_INITIATIVE_IN_PROGRESS, VALIDATE_PROJECT_PERIOD),
			initiative,
			initiative.getProject()
		);

		initiative.done();

		projectAsyncService.updateProjectProgress(initiative.getProject().getId());

		return new InitiativeDoneInfo(
			getTeamMemberUserSeqsToSendMsg(userSeq, initiative),
			initiative.getInitiativeToken(),
			initiative.getName(),
			initiative.getTeamMember().getUser().getUsername()
		);
	}

	@Override
	public Page<InitiativeInfo> getInitiativeByKeyResultToken(String keyResultToken, Long userSeq, Pageable pageable) {
		return initiativeRepository.findInitiativeByKeyResultTokenAndUserSeq(keyResultToken, userSeq, pageable)
			.map(InitiativeInfo::new);
	}

	@Override
	public FeedbackInfo registerFeedback(FeedbackSaveCommand command, Long requesterSeq) {
		Initiative initiative = initiativeRepository.findInitiativeForFeedbackByInitiativeTokenAndRequesterSeq(
				command.initiativeToken(), requesterSeq)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_INITIATIVE_TOKEN));

		validateProcessor.validate(
			List.of(VALIDATE_PROJECT_PERIOD, VALIDATE_INITIATIVE_DONE, VALIDATE_SELF_FEEDBACK),
			initiative.getProject(),
			initiative,
			initiative.getTeamMember(),
			requesterSeq
		);

		return new FeedbackInfo(feedbackRepository.save(command.toEntity(initiative, requesterSeq)));
	}

	@Override
	public InitiativeDetailInfo getInitiativeBy(String initiativeToken, Long userSeq) {
		return initiativeRepository.findInitiativeDetailByInitiativeTokenAndUserSeq(initiativeToken, userSeq)
			.map(initiative -> new InitiativeDetailInfo(initiative, userSeq))
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_INITIATIVE_TOKEN));
	}

	@Override
	public List<InitiativeForCalendarInfo> getInitiativeByDate(LocalDate searchDate, Long userSeq) {
		return initiativeRepository.findInitiativeByDate(searchDate, userSeq)
			.stream()
			.map(InitiativeForCalendarInfo::new)
			.toList();
	}

	@Override
	public List<String> getInitiativeDatesBy(YearMonth yearMonth, Long userSeq) {
		LocalDate monthEndDt = yearMonth.atEndOfMonth();
		LocalDate monthStDt = monthEndDt.minusDays(monthEndDt.lengthOfMonth() - 1);
		List<Initiative> initiative = initiativeRepository.findInitiativeBySdtAndEdtAndUserSeq(
			monthStDt, monthEndDt, userSeq);

		return initiative.stream()
			.map(i -> getFromDate(monthStDt, i)
				.datesUntil(getToDate(monthEndDt, i))
				.map(LocalDate::toString)
				.collect(Collectors.toList()))
			.flatMap(Collection::stream)
			.distinct()
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@Override
	public IniFeedbackInfo getInitiativeFeedbacksBy(String initiativeToken, Long userSeq) {
		Initiative initiative =
			initiativeRepository.findInitiativeForFeedbackByInitiativeTokenAndRequesterSeq(
				initiativeToken, userSeq
			).orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_INITIATIVE_TOKEN));

		List<Feedback> feedbacks =
			feedbackRepository.findInitiativeFeedbacksByInitiativeToken(initiativeToken);

		return getIniFeedbackinfoFrom(userSeq, initiative, feedbacks);
	}

	@Override
	public Integer getCountOfInitiativeToGiveFeedback(Long userSeq) {
		List<Initiative> countOfInitiativeToGiveFeedback =
			initiativeRepository.getCountOfInitiativeToGiveFeedback(userSeq);
		return countOfInitiativeToGiveFeedback.size();
	}

	@Override
	public Page<FeedbackDetailInfo> getRecievedFeedback(SearchRange range, Long userSeq, Pageable pageable) {
		return feedbackRepository.getRecievedFeedback(range, userSeq, pageable)
			.map(FeedbackDetailInfo::new);
	}

	@Override
	public List<ParticipateProjectInfo> getParticipateProjects(final Long userSeq) {
		return projectRepository.findParticipateProjectByUserSeq(userSeq)
			.stream()
			.map(project -> new ParticipateProjectInfo(project, userSeq))
			.toList();
	}

	@Transactional
	@Override
	public void promoteNextProjectLeaderOrDeleteProject(final Long userSeq) {
		final List<Project> participateProjectList = projectRepository.findParticipateProjectByUserSeq(userSeq);

		participateProjectList.forEach(project -> {
			if (project.getType() == ProjectType.SINGLE) {
				projectRepository.delete(project);
			} else {
				getLeaderWhenRequesterIsLeader(userSeq, project).ifPresent(
					teamMember -> processPromoteNewLeaderOrDelete(project, teamMember)
				);
			}
		});
	}

	@Override
	public List<InitiativeInfo> getRequiredFeedbackInitiative(final Long userSeq) {
		return initiativeRepository.getCountOfInitiativeToGiveFeedback(userSeq).stream()
			.map(InitiativeInfo::new)
			.toList();
	}

	private void processPromoteNewLeaderOrDelete(final Project project, final TeamMember teamMember) {
		project.getNextProjectLeader()
			.ifPresentOrElse(
				nextProjectLeader -> {
					nextProjectLeader.makeMemberAsProjectLeader();
					teamMember.changeLeaderRoleToMember();
				},
				() -> projectRepository.delete(project)
			);
	}

	private static Optional<TeamMember> getLeaderWhenRequesterIsLeader(final Long userSeq, final Project project) {
		final Optional<TeamMember> leaderUser = project.getTeamMember()
			.stream()
			.filter(tm -> tm.getUserSeq().equals(userSeq))
			.findFirst();
		return leaderUser;
	}

	private void addInitiative(ProjectInitiativeSaveCommand command, Project project, Initiative initiative) {
		project.getKeyResults()
			.stream()
			.filter(kr -> kr.getKeyResultToken().equals(command.keyResultToken()))
			.findFirst()
			.orElseThrow()
			.addInitiative(initiative);
	}

	private TeamMember getTeamMember(Long userSeq, Project project) {
		return project.getTeamMember()
			.stream()
			.filter(tm -> tm.getUser().getUserSeq().equals(userSeq))
			.findFirst()
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN));
	}

	private Project inviteUserValidator(String projectToken, Long invitedUserSeq, Long inviterSeq) {
		if (inviterSeq.equals(invitedUserSeq))
			throw new OkrApplicationException(ErrorCode.NOT_AVAIL_INVITE_MYSELF);

		Project project = projectRepository.findFetchedTeamMemberByProjectTokenAndUser(projectToken, inviterSeq)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN));

		validateProcessor.validate(List.of(VALIDATE_LEADER, VALIDATE_PROJECT_PERIOD), project, inviterSeq);

		project.validateTeamMember(invitedUserSeq);

		return project;
	}

	private Initiative buildInitiative(ProjectInitiativeSaveCommand command, TeamMember teamMember) {
		return Initiative.builder()
			.edt(command.edt())
			.sdt(command.sdt())
			.name(command.name())
			.detail(command.detail())
			.teamMember(teamMember)
			.build();
	}

	private LocalDate getFromDate(LocalDate monthStDt, Initiative i) {
		return i.getSdt().isAfter(monthStDt) ? i.getSdt() : monthStDt;
	}

	private LocalDate getToDate(LocalDate monthEndDt, Initiative i) {
		return (i.getEdt().isBefore(monthEndDt) ? i.getEdt() : monthEndDt).plusDays(1);
	}

	private List<Long> getTeamMemberUserSeqsToSendMsg(Long userSeq, Initiative initiative) {
		return initiative.getProject().getTeamMember().stream()
			.map(TeamMember::getUserSeq)
			.filter(user -> !user.equals(userSeq))
			.toList();
	}

	private IniFeedbackInfo getIniFeedbackinfoFrom(Long userSeq, Initiative initiative, List<Feedback> feedbacks) {
		boolean isRequestersFeedback = initiative.getTeamMember().getUserSeq().equals(userSeq);
		boolean wroteFeedback = !isRequestersFeedback && feedbacks.stream().anyMatch(f -> f.getTeamMember().getUserSeq().equals(userSeq));

		return new IniFeedbackInfo(
			isRequestersFeedback,
			wroteFeedback,
			feedbacks.stream()
				.collect(Collectors.groupingBy(Feedback::getGrade, Collectors.counting())),
			feedbacks.stream()
				.map(FeedbackDetailInfo::new)
				.collect(Collectors.toList())
		);
	}

}
