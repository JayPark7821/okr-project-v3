package kr.jay.okrver3.application.feedback;

import org.springframework.stereotype.Service;

import kr.jay.okrver3.domain.feedback.service.FeedbackService;
import kr.jay.okrver3.domain.notification.service.NotificationService;
import kr.jay.okrver3.domain.project.service.ProjectService;
import kr.jay.okrver3.domain.project.service.info.ProjectInitiativeInfo;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.interfaces.feedback.FeedbackSaveCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackFacade {

	private final FeedbackService feedbackService;
	private final ProjectService projectService;
	private final NotificationService notificationService;

	public String registerFeedback(FeedbackSaveCommand command, User requester) {
		ProjectInitiativeInfo projectInitiativeInfo =
			projectService.getProjectInitiativeInfoByInitiativeTokenAndUser(
				command.initiativeToken(),
				requester
			);
		String feedbackToken = feedbackService.registerFeedback(command, requester);
		notificationService.sendFeedbackNotification(projectInitiativeInfo.initiativeName(), requester);
		return feedbackToken;
	}
}
