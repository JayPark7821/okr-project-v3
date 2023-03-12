package kr.jay.okrver3.domain.project;

import static kr.jay.okrver3.domain.project.validator.ProjectValidatorType.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.project.aggregate.feedback.Feedback;
import kr.jay.okrver3.domain.project.aggregate.feedback.FeedbackRepository;
import kr.jay.okrver3.domain.project.aggregate.feedback.SearchRange;
import kr.jay.okrver3.domain.project.aggregate.initiative.Initiative;
import kr.jay.okrver3.domain.project.aggregate.initiative.InitiativeRepository;
import kr.jay.okrver3.domain.project.aggregate.keyresult.KeyResult;
import kr.jay.okrver3.domain.project.aggregate.team.TeamMember;
import kr.jay.okrver3.domain.project.command.FeedbackSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectDetailRetrieveCommand;
import kr.jay.okrver3.domain.project.command.ProjectInitiativeSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectKeyResultSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectSaveCommand;
import kr.jay.okrver3.domain.project.info.FeedbackDetailInfo;
import kr.jay.okrver3.domain.project.info.FeedbackInfo;
import kr.jay.okrver3.domain.project.info.IniFeedbackInfo;
import kr.jay.okrver3.domain.project.info.InitiativeDetailInfo;
import kr.jay.okrver3.domain.project.info.InitiativeForCalendarInfo;
import kr.jay.okrver3.domain.project.info.InitiativeInfo;
import kr.jay.okrver3.domain.project.info.ProjectDetailInfo;
import kr.jay.okrver3.domain.project.info.ProjectInfo;
import kr.jay.okrver3.domain.project.info.ProjectSideMenuInfo;
import kr.jay.okrver3.domain.project.info.ProjectTeamMembersInfo;
import kr.jay.okrver3.domain.project.validator.ProjectValidateProcessor;
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
	public String registerInitiative(ProjectInitiativeSaveCommand command, Long userSeq) {

		Project project = projectRepository.findByKeyResultTokenAndUser(command.keyResultToken(), userSeq)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_KEYRESULT_TOKEN));

		Initiative initiative = buildInitiative(command, getTeamMember(userSeq, project));

		validateProcessor.validate(
			List.of(VALIDATE_PROJECT_PERIOD, VALIDATE_PROJECT_INITIATIVE_DATE),
			project,
			initiative
		);

		getKeyResult(command, project)
			.addInitiative(
				initiative
			);

		updateProjectProgress(initiative.getProject().getId());
		return initiative.getInitiativeToken();
	}

	@Override
	public String initiativeFinished(String initiativeToken, Long userSeq) {
		Initiative initiative =
			initiativeRepository.findInitiativeByInitiativeTokenAndUserSeq(initiativeToken, userSeq)
				.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_INITIATIVE_TOKEN));

		validateProcessor.validate(
			List.of(VALIDATE_INITIATIVE_IN_PROGRESS, VALIDATE_PROJECT_PERIOD),
			initiative,
			initiative.getProject()
		);

		initiative.done();
		updateProjectProgress(initiative.getProject().getId());
		return initiative.getInitiativeToken();
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
			.map(initiative-> new InitiativeDetailInfo(initiative, userSeq))
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

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	void updateProjectProgress(Long projectId) {
		Project projectReference = projectRepository.findProjectForUpdateById(projectId)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN));
		projectReference.updateProgress(projectRepository.getProjectProgress(projectId));
	}

	private KeyResult getKeyResult(ProjectInitiativeSaveCommand command, Project project) {
		return project.getKeyResults()
			.stream()
			.filter(kr -> kr.getKeyResultToken().equals(command.keyResultToken()))
			.findFirst()
			.orElseThrow();
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

	private IniFeedbackInfo getIniFeedbackinfoFrom(Long userSeq, Initiative initiative, List<Feedback> feedbacks) {
		boolean isRequestersFeedback = initiative.getTeamMember().getUserSeq().equals(userSeq);
		boolean wroteFeedback = !isRequestersFeedback ?
			feedbacks.stream().filter(f -> f.getTeamMember().getUserSeq().equals(userSeq)).findFirst().isPresent() : false;

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
