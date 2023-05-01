package kr.service.okr.api.project;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import kr.service.okr.application.project.RegisterInitiativeCommand;
import kr.service.okr.project.api.RegisterInitiativeRequest;

public class InitiativeDtoMapper {
	static RegisterInitiativeCommand of(final RegisterInitiativeRequest requestDto) {

		return new RegisterInitiativeCommand(
			requestDto.keyResultToken(),
			requestDto.name(),
			LocalDate.parse(requestDto.startDate(), DateTimeFormatter.ISO_DATE),
			LocalDate.parse(requestDto.endDate(), DateTimeFormatter.ISO_DATE),
			requestDto.detail()
		);
	}
}
