package kr.service.okr.application.project;

import java.time.LocalDate;

public record RegisterInitiativeCommand(
	String keyResultToken,
	String name,
	LocalDate startDate,
	LocalDate endDate,
	String detail
) {
}
