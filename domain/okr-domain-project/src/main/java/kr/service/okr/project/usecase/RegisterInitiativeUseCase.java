package kr.service.okr.project.usecase;

import java.time.LocalDate;

public interface RegisterInitiativeUseCase {

	String command(Command command);

	record Command(
		String keyResultToken,
		String name,
		LocalDate startDate,
		LocalDate endDate,
		String detail,
		Long requesterSeq
	) {
	}

}
