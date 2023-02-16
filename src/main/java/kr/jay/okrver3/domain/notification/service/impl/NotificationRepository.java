package kr.jay.okrver3.domain.notification.service.impl;

import java.util.List;

import kr.jay.okrver3.domain.notification.Notification;

public interface NotificationRepository {
	void bulkInsert(List<Notification> notificationStream);
}
