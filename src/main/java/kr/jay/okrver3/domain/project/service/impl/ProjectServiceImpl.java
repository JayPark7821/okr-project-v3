package kr.jay.okrver3.domain.project.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.jay.okrver3.application.project.ProjectInitiativeSaveCommand;
import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.initiative.Initiative;
import kr.jay.okrver3.domain.keyresult.KeyResult;
import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.project.service.ProjectDetailInfo;
import kr.jay.okrver3.domain.project.service.ProjectInfo;
import kr.jay.okrver3.domain.project.service.ProjectRepository;
import kr.jay.okrver3.domain.project.service.ProjectService;
import kr.jay.okrver3.domain.project.service.ProjectTeamMemberInfo;
import kr.jay.okrver3.domain.project.validator.ProjectValidateProcessor;
import kr.jay.okrver3.domain.project.validator.ProjectValidateProcessorType;
import kr.jay.okrver3.domain.team.TeamMember;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.interfaces.project.ProjectDetailRetrieveCommand;
import kr.jay.okrver3.interfaces.project.ProjectKeyResultSaveDto;
import kr.jay.okrver3.interfaces.project.ProjectMasterSaveDto;
import kr.jay.okrver3.interfaces.project.ProjectSideMenuResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;
	private final ProjectValidateProcessor validateProcessor;

	@Transactional
	@Override
	public ProjectInfo registerProject(ProjectMasterSaveDto dto, User user, List<User> teamMemberUsers) {
		Project project = projectRepository.save(buildProjectFrom(dto));
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
	public ProjectTeamMemberInfo inviteTeamMember(String projectToken, User invitedUser, User inviter) {
		Project project = inviteUserValidator(projectToken, invitedUser.getEmail(), inviter);
		project.addTeamMember(invitedUser);
		return new ProjectTeamMemberInfo(project.getTeamMember().stream().map(TeamMember::getUser).toList(),
			project.getObjective());
	}

	@Override
	public void validateUserToInvite(String projectToken, String invitedUserEmail, User user) {
		inviteUserValidator(projectToken, invitedUserEmail, user);
	}

	@Override
	public Page<ProjectDetailInfo> getDetailProjectList(ProjectDetailRetrieveCommand command) {
		return projectRepository.getDetailProjectList(command);
	}

	@Override
	public ProjectSideMenuResponse getProjectSideMenuDetails(String projectToken, User user) {
		return projectRepository.findProgressAndTeamMembersByProjectTokenAndUser(projectToken, user)
			.map(ProjectSideMenuResponse::new)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN));
	}

	@Transactional
	@Override
	public String registerKeyResult(ProjectKeyResultSaveDto dto, User user) {
		Project project = projectRepository.findProjectKeyResultByProjectTokenAndUser(
			dto.projectToken(), user).orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN));

		validateProcessor.validate(ProjectValidateProcessorType.ADD_KEYRESULT_VALIDATION, project, user);

		return project.addKeyResult(dto.keyResultName());
	}

	@Transactional
	@Override
	public String registerInitiative(ProjectInitiativeSaveCommand command, User user) {
		Project project = projectRepository.findByKeyResultTokenAndUser(command.keyResultToken(), user)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_KEYRESULT_TOKEN));

		validateProcessor.validate(ProjectValidateProcessorType.ADD_INITIATIVE_VALIDATION, project, command);

		Initiative initiative = buildInitiative(command, getTeamMember(user, project));

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
		Initiative initiative = projectRepository.findProjectInitiativeByInitiativeTokenAndUser(initiativeToken, user)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_INITIATIVE_TOKEN));

		validateProcessor.validate(
			ProjectValidateProcessorType.INITIATIVE_FINISH_VALIDATION,
			initiative.getProject(),
			initiative
		);
		initiative.done();
		updateProjectProgress(initiative.getProject().getId());
		return initiative.getInitiativeToken();
	}

	void updateProjectProgress(Long projectId) {
		Project projectReference = projectRepository.getReferenceById(projectId);
		projectReference.updateProgress(projectRepository.getProjectProgress(projectId));
	}


	private KeyResult getKeyResult(ProjectInitiativeSaveCommand command, Project project) {
		KeyResult keyResult = project.getKeyResults()
			.stream()
			.filter(kr -> kr.getKeyResultToken().equals(command.keyResultToken()))
			.findFirst()
			.orElseThrow();
		return keyResult;
	}

	private TeamMember getTeamMember(User user, Project project) {
		TeamMember teamMember = project.getTeamMember()
			.stream()
			.filter(tm -> tm.getUser().equals(user))
			.findFirst()
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN));
		return teamMember;
	}

	private Project inviteUserValidator(String projectToken, String invitedUserEmail, User user) {
		if (user.getEmail().equals(invitedUserEmail))
			throw new OkrApplicationException(ErrorCode.NOT_AVAIL_INVITE_MYSELF);

		Project project = projectRepository.findFetchedTeamMemberByProjectTokenAndUser(projectToken, user)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN));

		validateProcessor.validate(ProjectValidateProcessorType.PROJECT_BASIC_VALIDATION, project, user);

		project.validateEmail(invitedUserEmail);

		return project;
	}

	private Project buildProjectFrom(ProjectMasterSaveDto dto) {
		LocalDate startDt = LocalDate.parse(dto.sdt(), DateTimeFormatter.ISO_DATE);
		LocalDate endDt = LocalDate.parse(dto.sdt(), DateTimeFormatter.ISO_DATE);

		return Project.builder()
			.startDate(startDt)
			.endDate(endDt)
			.objective(dto.objective())
			.progress(0)
			.keyResultList(dto.keyResults())
			.build();
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
