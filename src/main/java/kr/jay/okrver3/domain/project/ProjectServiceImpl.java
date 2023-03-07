package kr.jay.okrver3.domain.project;

import static kr.jay.okrver3.domain.project.validator.ProjectValidatorType.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.project.aggregate.feedback.FeedbackRepository;
import kr.jay.okrver3.domain.project.aggregate.initiative.Initiative;
import kr.jay.okrver3.domain.project.aggregate.initiative.InitiativeRepository;
import kr.jay.okrver3.domain.project.aggregate.keyresult.KeyResult;
import kr.jay.okrver3.domain.project.aggregate.team.TeamMember;
import kr.jay.okrver3.domain.project.command.FeedbackSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectDetailRetrieveCommand;
import kr.jay.okrver3.domain.project.command.ProjectInitiativeSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectKeyResultSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectSaveCommand;
import kr.jay.okrver3.domain.project.info.ProjectDetailInfo;
import kr.jay.okrver3.domain.project.info.ProjectInfo;
import kr.jay.okrver3.domain.project.info.ProjectInitiativeInfo;
import kr.jay.okrver3.domain.project.info.ProjectSideMenuInfo;
import kr.jay.okrver3.domain.project.info.ProjectTeamMembersInfo;
import kr.jay.okrver3.domain.project.validator.ProjectValidateProcessor;
import kr.jay.okrver3.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;
	private final InitiativeRepository initiativeRepository;
	private final FeedbackRepository feedbackRepository;
	private final ProjectValidateProcessor validateProcessor;

	@Transactional
	@Override
	public ProjectInfo registerProject(ProjectSaveCommand command, User user, List<User> teamMemberUsers) {
		Project project = projectRepository.save(command.toEntity());
		project.addLeader(user);
		teamMemberUsers.forEach(project::addTeamMember);
		return new ProjectInfo(project);
	}

	@Override
	public ProjectInfo getProjectInfoBy(String projectToken, User user) {
		return projectRepository.findByProjectTokenAndUser(projectToken, user)
			.map(ProjectInfo::new)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN));
	}

	@Override
	public ProjectTeamMembersInfo inviteTeamMember(String projectToken, User invitedUser, User inviter) {
		Project project = inviteUserValidator(projectToken, invitedUser.getEmail(), inviter);
		project.addTeamMember(invitedUser);
		return new ProjectTeamMembersInfo(project.getTeamMember().stream().map(TeamMember::getUser).toList(),
			project.getObjective());
	}

	@Override
	public void validateUserToInvite(String projectToken, String invitedUserEmail, User user) {
		inviteUserValidator(projectToken, invitedUserEmail, user);
	}

	@Override
	public Page<ProjectDetailInfo> getDetailProjectList(ProjectDetailRetrieveCommand command, User user) {
		return projectRepository.getDetailProjectList(command, user)
			.map(project -> new ProjectDetailInfo(project, user.getEmail()));
	}

	@Override
	public ProjectSideMenuInfo getProjectSideMenuDetails(String projectToken, User user) {
		return projectRepository.findProgressAndTeamMembersByProjectTokenAndUser(projectToken, user)
			.map(ProjectSideMenuInfo::new)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN));
	}

	@Transactional
	@Override
	public String registerKeyResult(ProjectKeyResultSaveCommand command, User user) {
		Project project = projectRepository.findProjectKeyResultByProjectTokenAndUser(
				command.projectToken(), user)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN));

		validateProcessor.validate(
			List.of(VALIDATE_LEADER, VALIDATE_PROJECT_PERIOD, VALIDATE_KEYRESULT_COUNT),
			project,
			user
		);

		return project.addKeyResult(command.keyResultName());
	}

	@Transactional
	@Override
	public String registerInitiative(ProjectInitiativeSaveCommand command, User user) {

		Project project = projectRepository.findByKeyResultTokenAndUser(command.keyResultToken(), user)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_KEYRESULT_TOKEN));

		Initiative initiative = buildInitiative(command, getTeamMember(user, project));

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

	@Transactional
	@Override
	public String initiativeFinished(String initiativeToken, User user) {
		Initiative initiative =
			initiativeRepository.findInitiativeByInitiativeTokenAndUser(initiativeToken, user)
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
	public Page<ProjectInitiativeInfo> getInitiativeByKeyResultToken(String keyResultToken, User user,
		Pageable pageable) {
		return initiativeRepository.findInitiativeByKeyResultTokenAndUser(
			keyResultToken, user, pageable
		).map(ProjectInitiativeInfo::new);
	}

	@Override
	public String registerFeedback(FeedbackSaveCommand command, User requester) {
		Initiative initiative = initiativeRepository.findInitiativeForFeedbackByInitiativeTokenAndRequester(
				command.initiativeToken(), requester)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_INITIATIVE_TOKEN));

		validateProcessor.validate(
			List.of(VALIDATE_PROJECT_PERIOD, VALIDATE_INITIATIVE_DONE, VALIDATE_SELF_FEEDBACK),
			initiative.getProject(),
			initiative,
			initiative.getTeamMember(),
			requester
		);

		return feedbackRepository.save(command.toEntity(initiative, requester)).getFeedbackToken();
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

	private TeamMember getTeamMember(User user, Project project) {
		return project.getTeamMember()
			.stream()
			.filter(tm -> tm.getUser().equals(user))
			.findFirst()
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN));
	}

	private Project inviteUserValidator(String projectToken, String invitedUserEmail, User user) {
		if (user.getEmail().equals(invitedUserEmail))
			throw new OkrApplicationException(ErrorCode.NOT_AVAIL_INVITE_MYSELF);

		Project project = projectRepository.findFetchedTeamMemberByProjectTokenAndUser(projectToken, user)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN));

		validateProcessor.validate(List.of(VALIDATE_LEADER, VALIDATE_PROJECT_PERIOD), project, user);

		project.validateEmail(invitedUserEmail);

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

}
