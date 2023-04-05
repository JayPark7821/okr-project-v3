package kr.service.okr.project.usecase;

import java.time.LocalDate;
import java.util.List;

public interface RegisterProjectUseCase {

	String registerProject(Command command);

	record Command(
		String objective,
		LocalDate startDate,
		LocalDate endDate,
		Long userSeq,
		List<Long> teamMemberUserSeqs
	) {
	}
}
