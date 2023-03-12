package kr.jay.okrver3.domain.notification.info;

import kr.jay.okrver3.domain.notification.Notification;
import kr.jay.okrver3.domain.notification.NotificationCheckType;
import kr.jay.okrver3.domain.notification.Notifications;

public record NotificationInfo(
	String notiToken,
	Notifications notiType,
	String msg,
	NotificationCheckType status,
	String createdDate
) {

	public NotificationInfo(Notification notification) {
		this(
			notification.getNotificationToken(),
			notification.getType(),
			notification.getMsg(),
			notification.getStatus(),
			notification.getCreatedDate().toString()
		);
	}
}
