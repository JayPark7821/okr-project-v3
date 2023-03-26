package kr.service.okr.domain.notification;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.service.okr.domain.notification.info.NotificationInfo;

public interface NotificationService {
	void sendNotification(Notifications notificationType, List<Long> notificationReceiveUserSeq, String... args);

	void sendNotification(Notifications notificationType, Long notificationReceiveUserSeq, String... args);

	Page<NotificationInfo> getNotifications(Pageable pageable, Long userSeq);

	void checkNotification(String notificationToken, Long userSeq);

	void deleteNotification(String notificationToken, Long userSeq);
}
