package kr.service.okr.domain.notification.info;

import kr.service.okr.domain.notification.Notification;
import kr.service.okr.domain.notification.NotificationCheckType;

public record NotificationInfo(
	String notiToken,
	String msg,
	NotificationCheckType status,
	String createdDate
) {

	public NotificationInfo(Notification notification) {
		this(
			notification.getNotificationToken(),
			notification.getMsg(),
			notification.isChecked() ? NotificationCheckType.CHECKED : NotificationCheckType.NEW,
			notification.getCreatedDate().toString()
		);
	}
}
