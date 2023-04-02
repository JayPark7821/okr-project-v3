package kr.service.okr.application.project;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kr.service.okr.domain.notification.NotificationService;
import kr.service.okr.domain.project.ProjectAsyncService;
import kr.service.okr.domain.project.ProjectService;
import kr.service.okr.domain.project.aggregate.feedback.SearchRange;
import kr.service.okr.domain.project.command.FeedbackSaveCommand;
import kr.service.okr.domain.project.command.ProjectDetailRetrieveCommand;
import kr.service.okr.domain.project.command.ProjectInitiativeSaveCommand;
import kr.service.okr.domain.project.command.ProjectKeyResultSaveCommand;
import kr.service.okr.domain.project.command.ProjectSaveCommand;
import kr.service.okr.domain.project.command.TeamMemberInviteCommand;
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
import kr.service.okr.domain.user.info.UserInfo;
import kr.service.okr.domain.user.service.UserService;
import kr.service.okrcommon.common.enums.Notifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectFacade {

	private final ProjectService projectService;
	private final UserService userService;
	private final NotificationService notificationService;
	private final ProjectAsyncService projectAsyncService;

	public String registerProject(ProjectSaveCommand command, Long userSeq) {

		List<Long> invitedUserSeq =
			command.teamMembers() != null ? getTeamUsersFromEmails(command.teamMembers()) : List.of();

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

		notificationService.sendNotification(
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
		final InitiativeSavedInfo savedInfo = projectService.registerInitiative(command, userSeq);
		log.info("============ update progress end");
		projectAsyncService.updateProjectProgress(savedInfo.projectId());
		return savedInfo.initiativeToken();
	}

	public String initiativeFinished(String initiativeToken, Long userSeq) {
		InitiativeDoneInfo info = projectService.initiativeFinished(initiativeToken, userSeq);
		notificationService.sendNotification(Notifications.INITIATIVE_ACHIEVED, info.teamMemberUserSeqs(),
			info.username(), info.initiativeName());
		return info.initiativeToken();
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
		return projectService.getRecievedFeedback(range, userSeq, pageable);
	}

	public List<ParticipateProjectInfo> getParticipateProjects(final Long userSeq) {
		return projectService.getParticipateProjects(userSeq);
	}

	public List<InitiativeInfo> getRequiredFeedbackInitiative(final Long userSeq) {
		return projectService.getRequiredFeedbackInitiative(userSeq);
	}
}
