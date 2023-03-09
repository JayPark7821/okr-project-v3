package kr.jay.okrver3.interfaces.project.response;

public record InitiativeForCalendarResponse(
	String initiativeToken,
	String initiativeName,
	String startDate,
	String endDate) {
}
