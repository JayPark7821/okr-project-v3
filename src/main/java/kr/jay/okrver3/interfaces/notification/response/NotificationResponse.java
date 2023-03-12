package kr.jay.okrver3.interfaces.notification.response;

public record NotificationResponse(
	String notiToken,
	String notiType,
	String msg,
	String status,
	String createdDate
) {
}
