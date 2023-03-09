package kr.jay.okrver3.domain.project.info;

import java.time.format.DateTimeFormatter;

import kr.jay.okrver3.domain.project.aggregate.initiative.Initiative;

public record InitiativeForCalendarInfo(
	String initiativeToken,
	String initiativeName,
	String startDate,
	String endDate
) {

	public InitiativeForCalendarInfo(Initiative initiative) {
		this(
			initiative.getInitiativeToken(),
			initiative.getName(),
			initiative.getSdt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
			initiative.getEdt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
		);
	}
}
