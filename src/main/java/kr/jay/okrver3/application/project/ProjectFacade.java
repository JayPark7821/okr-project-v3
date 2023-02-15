package kr.jay.okrver3.application.project;

import org.springframework.stereotype.Service;

import kr.jay.okrver3.domain.notification.service.NotificationService;
import kr.jay.okrver3.domain.project.service.ProjectInfo;
import kr.jay.okrver3.domain.project.service.ProjectService;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.domain.user.service.UserService;
import kr.jay.okrver3.interfaces.project.ProjectMasterSaveDto;
import kr.jay.okrver3.interfaces.project.TeamMemberInviteRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectFacade {

	private final ProjectService projectService;
	private final UserService userService;
	private final NotificationService notificationService;

	public String registerProject(ProjectMasterSaveDto dto, User user) {
		ProjectInfo projectInfo = projectService.registerProject(dto);
		return projectInfo.projectToken();
	}

	public ProjectInfo getProjectInfoBy(String projectToken, User user) {
		return projectService.getProjectInfoBy(projectToken, user);
	}

	public String inviteTeamMember(TeamMemberInviteRequestDto teamMemberInviteRequestDto, User user) {
		User invitedUser = userService.findByEmail(teamMemberInviteRequestDto.email())
			.orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자가 존재하지 않습니다."));

		projectService.inviteTeamMember(teamMemberInviteRequestDto.projectToken(), invitedUser, user);

		notificationService.sendInvitationNotification(invitedUser, user);
		return
	}
}
