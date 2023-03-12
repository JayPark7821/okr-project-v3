package kr.jay.okrver3.application.project;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kr.jay.okrver3.domain.notification.NotificationService;
import kr.jay.okrver3.domain.notification.Notifications;
import kr.jay.okrver3.domain.project.ProjectService;
import kr.jay.okrver3.domain.project.aggregate.feedback.SearchRange;
import kr.jay.okrver3.domain.project.command.FeedbackSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectDetailRetrieveCommand;
import kr.jay.okrver3.domain.project.command.ProjectInitiativeSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectKeyResultSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectSaveCommand;
import kr.jay.okrver3.domain.project.command.TeamMemberInviteCommand;
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
import kr.jay.okrver3.domain.user.info.UserInfo;
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

		List<Long> invitedUserSeq = command.teamMembers() != null ? getTeamUsersFromEmails(command.teamMembers()) : List.of();

		return projectService.registerProject(
			command,
			userSeq,
			invitedUserSeq
		).projectToken();
	}

	public ProjectInfo getProjectInfoBy(String projectToken, Long userSeq) {
		return projectService.getProjectInfoBy(projectToken, userSeq);
	}

	public String inviteTeamMember(TeamMemberInviteCommand command, Long inviterSeq) {

		UserInfo invitedUser = userService.findUserInfoBy(command.email());

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

	public String validateEmailToInvite(String projectToken, String email, Long userSeq) {
		UserInfo invitedUserInfo = userService.findUserInfoBy(email);
		projectService.validateUserToInvite(projectToken, invitedUserInfo.userSeq(), userSeq);
		return email;
	}

	private List<Long> getTeamUsersFromEmails(List<String> teamMembers) {
		return teamMembers.stream().map(userService::findUserInfoBy)
			.map(UserInfo::userSeq).toList();
		// TODO
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

	public InitiativeDetailInfo getInitiativeBy(String initiativeToken, Long userSeq) {
		return projectService.getInitiativeBy(initiativeToken, userSeq);
	}

	public List<InitiativeForCalendarInfo> getInitiativeByDate(LocalDate searchDate, Long userSeq) {
		return projectService.getInitiativeByDate(searchDate, userSeq);
	}

	public List<String> getInitiativeDatesBy(YearMonth yearMonth, Long userSeq) {
		return projectService.getInitiativeDatesBy(yearMonth, userSeq);
	}

	public IniFeedbackInfo getInitiativeFeedbacksBy(String initiativeToken, Long userSeq) {
		return projectService.getInitiativeFeedbacksBy(initiativeToken, userSeq);
	}

	public Integer getCountOfInitiativeToGiveFeedback(Long userSeq) {
		return projectService.getCountOfInitiativeToGiveFeedback(userSeq);
	}

	public Page<FeedbackDetailInfo> getRecievedFeedback(SearchRange range, Long userSeq, Pageable pageable) {
		throw new UnsupportedOperationException(
			"kr.jay.okrver3.application.project.ProjectFacade.getRecievedFeedback()");
	}
}
