package kr.jay.okrver3.application.project;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.notification.service.NotificationService;
import kr.jay.okrver3.domain.project.service.ProjectInfo;
import kr.jay.okrver3.domain.project.service.ProjectService;
import kr.jay.okrver3.domain.project.service.ProjectTeamMemberInfo;
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

		List<User> teamMemberUsers =
			dto.teamMembers() != null ? getTeamUsersFromEmails(dto) : List.of();

		ProjectInfo projectInfo = projectService.registerProject(dto, userService.getReferenceById(user.getUserSeq()), teamMemberUsers);

		return projectInfo.projectToken();
	}

	public ProjectInfo getProjectInfoBy(String projectToken, User user) {
		return projectService.getProjectInfoBy(projectToken, user);
	}

	public String inviteTeamMember(TeamMemberInviteRequestDto requestDto, User inviter) {

		User invitedUser = userService.findByEmail(requestDto.email())
			.orElseThrow(() -> new IllegalArgumentException("해당 이메일의 사용자가 존재하지 않습니다."));

		ProjectTeamMemberInfo projectTeamMemberInfo = projectService.inviteTeamMember(requestDto.projectToken(),
			invitedUser, inviter);

		notificationService.sendInvitationNotification(
			getTeamMemberToSendNoti(invitedUser, projectTeamMemberInfo),
			projectTeamMemberInfo.projectName(),
			invitedUser.getUsername()
		);

		return invitedUser.getEmail();
	}

	public String validateEmail(String projectToken, String email, User user) {

		if(user.getEmail().equals(email))
			throw new OkrApplicationException(ErrorCode.NOT_AVAIL_INVITE_MYSELF);

		User invitedUser = userService.findByEmail(email)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_USER_EMAIL));

		projectService.validateUserToInvite(projectToken, invitedUser.getEmail(), user);

		return email;
	}

	private List<User> getTeamUsersFromEmails(ProjectMasterSaveDto dto) {
		return dto.teamMembers().stream().map(userService::findByEmail)
			.filter(Optional::isPresent)
			.map(Optional::get).toList();
	}

	private static List<User> getTeamMemberToSendNoti(User invitedUser, ProjectTeamMemberInfo projectTeamMemberInfo) {
		return projectTeamMemberInfo.projectTeamMemberUsers()
			.stream()
			.filter(user -> !user.equals(invitedUser))
			.toList();
	}
}
