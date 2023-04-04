package kr.service.project.project.usecase;

import java.time.LocalDate;
import java.util.List;

import kr.service.project.project.domain.Project;

public interface RegisterProjectUseCase {

	Project registerProject(Command command);

	record Command(
		String objective,
		LocalDate startDate,
		LocalDate endDate,
		Long userSeq,
		List<Long> teamMemberUserSeqs
	) {

	}
}
