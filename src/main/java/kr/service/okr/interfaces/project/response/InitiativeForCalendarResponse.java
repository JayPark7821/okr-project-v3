package kr.service.okr.interfaces.project.response;

public record InitiativeForCalendarResponse(
	String initiativeToken,
	String initiativeName,
	String startDate,
	String endDate) {
}
