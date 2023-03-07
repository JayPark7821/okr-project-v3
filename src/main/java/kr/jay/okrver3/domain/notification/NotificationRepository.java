package kr.jay.okrver3.domain.notification;

import java.util.List;

public interface NotificationRepository {
	void bulkInsert(List<Notification> notificationStream);
}
