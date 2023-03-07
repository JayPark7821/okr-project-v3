package kr.jay.okrver3.application.project;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.notification.NotificationService;
import kr.jay.okrver3.domain.project.ProjectService;
import kr.jay.okrver3.domain.project.command.FeedbackSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectDetailRetrieveCommand;
import kr.jay.okrver3.domain.project.command.ProjectInitiativeSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectKeyResultSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectSaveCommand;
import kr.jay.okrver3.domain.project.command.TeamMemberInviteCommand;
import kr.jay.okrver3.domain.project.info.ProjectDetailInfo;
import kr.jay.okrver3.domain.project.info.ProjectInfo;
import kr.jay.okrver3.domain.project.info.ProjectInitiativeInfo;
import kr.jay.okrver3.domain.project.info.ProjectSideMenuInfo;
import kr.jay.okrver3.domain.project.info.ProjectTeamMembersInfo;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectFacade {

	private final ProjectService projectService;
	private final UserService userService;
	private final NotificationService notificationService;

	public String registerProject(ProjectSaveCommand command, User user) {

		List<User> teamMemberUsers =
			command.teamMembers() != null ? getTeamUsersFromEmails(command) : List.of();

		ProjectInfo projectInfo = projectService.registerProject(command,
			userService.getReferenceById(user.getUserSeq()),
			teamMemberUsers);

		return projectInfo.projectToken();
	}

	public ProjectInfo getProjectInfoBy(String projectToken, User user) {
		return projectService.getProjectInfoBy(projectToken, user);
	}

	public String inviteTeamMember(TeamMemberInviteCommand command, User inviter) {

		User invitedUser = getUserToInviteBy(command.email());

		ProjectTeamMembersInfo projectTeamMembersInfo = projectService.inviteTeamMember(
			command.projectToken(),
			invitedUser, inviter
		);

		notificationService.sendInvitationNotification(
			getTeamMemberToSendNoti(invitedUser, projectTeamMembersInfo),
			projectTeamMembersInfo.projectName(),
			invitedUser.getUsername()
		);

		return invitedUser.getEmail();
	}

	public String validateEmail(String projectToken, String email, User user) {
		projectService.validateUserToInvite(projectToken, getUserToInviteBy(email).getEmail(), user);
		return email;
	}

	private User getUserToInviteBy(String email) {
		return userService.findByEmail(email)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_USER_EMAIL));
	}

	private List<User> getTeamUsersFromEmails(ProjectSaveCommand command) {
		return command.teamMembers().stream().map(userService::findByEmail)
			.filter(Optional::isPresent)
			.map(Optional::get).toList();
	}

	private static List<User> getTeamMemberToSendNoti(User invitedUser, ProjectTeamMembersInfo info) {
		return info.projectTeamMemberUsers()
			.stream()
			.filter(user -> !user.equals(invitedUser))
			.toList();
	}

	public Page<ProjectDetailInfo> getDetailProjectList(ProjectDetailRetrieveCommand command, User user) {
		return projectService.getDetailProjectList(command, user);
	}

	public ProjectSideMenuInfo getProjectSideMenuDetails(String projectToken, User user) {
		return projectService.getProjectSideMenuDetails(projectToken, user);
	}

	public String registerKeyResult(ProjectKeyResultSaveCommand command, User user) {
		return projectService.registerKeyResult(command, user);
	}

	public String registerInitiative(ProjectInitiativeSaveCommand command, User user) {
		return projectService.registerInitiative(command, user);
	}

	public String initiativeFinished(String initiativeToken, User user) {
		return projectService.initiativeFinished(initiativeToken, user);
	}

	public Page<ProjectInitiativeInfo> getInitiativeByKeyResultToken(
		String keyResultToken,
		User user,
		Pageable pageable
	) {
		return projectService.getInitiativeByKeyResultToken(keyResultToken, user, pageable);
	}

	public String registerFeedback(FeedbackSaveCommand command, User requester) {
		return projectService.registerFeedback(command, requester);
	}
}
