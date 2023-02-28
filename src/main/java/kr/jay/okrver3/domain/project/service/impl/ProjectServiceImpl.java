package kr.jay.okrver3.domain.project.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.project.service.ProjectInfo;
import kr.jay.okrver3.domain.project.service.ProjectRepository;
import kr.jay.okrver3.domain.project.service.ProjectService;
import kr.jay.okrver3.domain.project.service.ProjectTeamMemberInfo;
import kr.jay.okrver3.domain.team.TeamMember;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.interfaces.project.ProjectMasterSaveDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;

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
		return new ProjectTeamMemberInfo(project.getTeamMember().stream().map(TeamMember::getUser).toList(),project.getObjective());
	}

	@Override
	public void validateUserToInvite(String projectToken, String invitedUserEmail, User user) {
		inviteUserValidator(projectToken, invitedUserEmail, user);
	}

	private Project inviteUserValidator(String projectToken, String invitedUserEmail, User user) {
		if(user.getEmail().equals(invitedUserEmail))
			throw new OkrApplicationException(ErrorCode.NOT_AVAIL_INVITE_MYSELF);

		Project project = projectRepository.findFetchedTeamMemberByProjectTokenAndUser(projectToken, user)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN));

		if (!isUserProjectLeader(user, project))
			throw new OkrApplicationException(ErrorCode.USER_IS_NOT_LEADER);

		project.validateEmail(invitedUserEmail);

		return project;
	}

	private boolean isUserProjectLeader(User user, Project project) {
		return project.getProjectLeader().getUser().equals(user);
	}


	private Project buildProjectFrom(ProjectMasterSaveDto dto) {
		LocalDate startDt = LocalDate.parse(dto.sdt(), DateTimeFormatter.ofPattern("yyyyMMdd"));
		LocalDate endDt = LocalDate.parse(dto.edt(), DateTimeFormatter.ofPattern("yyyyMMdd"));

		return Project.builder()
			.startDate(startDt)
			.endDate(endDt)
			.objective(dto.objective())
			.progress(0)
			.keyResultList(dto.keyResults())
			.build();
	}

}
