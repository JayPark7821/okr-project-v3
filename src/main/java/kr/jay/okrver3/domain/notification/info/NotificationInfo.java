package kr.jay.okrver3.domain.notification.info;

import kr.jay.okrver3.domain.notification.NotificationCheckType;
import kr.jay.okrver3.domain.notification.Notifications;

public record NotificationInfo(
	String notiToken,
	Notifications notiType,
	String msg,
	NotificationCheckType status,
	String createdDate
) {
}
