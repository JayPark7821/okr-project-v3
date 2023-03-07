package kr.jay.okrver3.domain.notification;

import java.util.List;

public interface NotificationService {
	void sendInvitationNotification(Notifications notificationType, List<Long> notiSendUserSeqs, String... args);

	void sendNotification(Notifications newFeedback, Long userSeq, String... args);
}
