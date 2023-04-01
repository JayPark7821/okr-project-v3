package kr.service.okr.interfaces.notification.response;

public record NotificationResponse(
	String notiToken,
	String msg,
	String status,
	String createdDate
) {
}
