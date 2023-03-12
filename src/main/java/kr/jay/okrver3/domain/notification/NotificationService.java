package kr.jay.okrver3.domain.notification;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.jay.okrver3.domain.notification.info.NotificationInfo;

public interface NotificationService {
	void sendInvitationNotification(Notifications notificationType, List<Long> notiSendUserSeqs, String... args);

	void sendNotification(Notifications newFeedback, Long userSeq, String... args);

	Page<NotificationInfo> getNotifications(Pageable pageable, Long userSeq);
}
