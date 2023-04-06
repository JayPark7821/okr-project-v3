package kr.service.okr.application;

import java.time.LocalDate;
import java.util.List;

public record RegisterProjectCommand(
	String objective,
	LocalDate startDate,
	LocalDate endDate,
	List<String> teamMembers,
	Long userSeq
) {
}
