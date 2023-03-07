package kr.jay.okrver3.application.project;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.notification.NotificationService;
import kr.jay.okrver3.domain.notification.Notifications;
import kr.jay.okrver3.domain.project.ProjectService;
import kr.jay.okrver3.domain.project.command.FeedbackSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectDetailRetrieveCommand;
import kr.jay.okrver3.domain.project.command.ProjectInitiativeSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectKeyResultSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectSaveCommand;
import kr.jay.okrver3.domain.project.command.TeamMemberInviteCommand;
import kr.jay.okrver3.domain.project.info.FeedbackInfo;
import kr.jay.okrver3.domain.project.info.InitiativeInfo;
import kr.jay.okrver3.domain.project.info.ProjectDetailInfo;
import kr.jay.okrver3.domain.project.info.ProjectInfo;
import kr.jay.okrver3.domain.project.info.ProjectSideMenuInfo;
import kr.jay.okrver3.domain.project.info.ProjectTeamMembersInfo;
import kr.jay.okrver3.domain.user.service.UserInfo;
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

	public String registerProject(ProjectSaveCommand command, Long userSeq) {

		ProjectInfo projectInfo =
			projectService.registerProject(
				command,
				userSeq,
				getTeamUsersFromEmails(command)
			);

		return projectInfo.projectToken();
	}

	public ProjectInfo getProjectInfoBy(String projectToken, Long userSeq) {
		return projectService.getProjectInfoBy(projectToken, userSeq);
	}

	public String inviteTeamMember(TeamMemberInviteCommand command, Long inviterSeq) {

		UserInfo invitedUser = getUserToInviteBy(command.email());

		ProjectTeamMembersInfo projectTeamMembersInfo =
			projectService.inviteTeamMember(
				command.projectToken(),
				invitedUser.userSeq(),
				inviterSeq
			);

		notificationService.sendInvitationNotification(
			Notifications.NEW_TEAM_MATE,
			getTeamMemberToSendNoti(invitedUser.userSeq(), projectTeamMembersInfo.teamMemberSeq()),
			invitedUser.name(),
			projectTeamMembersInfo.projectName()
		);

		return invitedUser.email();
	}

	public String validateEmail(String projectToken, String email, Long userSeq) {
		UserInfo invitedUserInfo = getUserToInviteBy(email);
		projectService.validateUserToInvite(projectToken, invitedUserInfo.userSeq(), userSeq);
		return email;
	}

	private UserInfo getUserToInviteBy(String email) {
		return userService.findByEmail(email)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_USER_EMAIL));
	}

	private List<Long> getTeamUsersFromEmails(ProjectSaveCommand command) {
		return command.teamMembers().stream().map(userService::findByEmail)
			.filter(Optional::isPresent)
			.map(user -> user.get().userSeq()).toList();
	}

	private List<Long> getTeamMemberToSendNoti(Long invitedUserSeq, List<Long> teamMemberUserSeq) {
		return teamMemberUserSeq
			.stream()
			.filter(user -> !Objects.equals(user, invitedUserSeq))
			.toList();
	}

	public Page<ProjectDetailInfo> getDetailProjectList(ProjectDetailRetrieveCommand command, Long userSeq) {
		return projectService.getDetailProjectList(command, userSeq);
	}

	public ProjectSideMenuInfo getProjectSideMenuDetails(String projectToken, Long userSeq) {
		return projectService.getProjectSideMenuDetails(projectToken, userSeq);
	}

	public String registerKeyResult(ProjectKeyResultSaveCommand command, Long userSeq) {
		return projectService.registerKeyResult(command, userSeq);
	}

	public String registerInitiative(ProjectInitiativeSaveCommand command, Long userSeq) {
		return projectService.registerInitiative(command, userSeq);
	}

	public String initiativeFinished(String initiativeToken, Long userSeq) {
		return projectService.initiativeFinished(initiativeToken, userSeq);
	}

	public Page<InitiativeInfo> getInitiativeByKeyResultToken(
		String keyResultToken,
		Long userSeq,
		Pageable pageable
	) {
		return projectService.getInitiativeByKeyResultToken(keyResultToken, userSeq, pageable);
	}

	public String registerFeedback(FeedbackSaveCommand command, Long requester) {
		FeedbackInfo feedbackInfo = projectService.registerFeedback(command, requester);
		notificationService.sendNotification(
			Notifications.NEW_FEEDBACK,
			feedbackInfo.initiativeUserSeq(),
			feedbackInfo.feedbackUserName(),
			feedbackInfo.initiativeName()
		);
		return feedbackInfo.feedbackToken();
	}
}
