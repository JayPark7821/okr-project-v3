package kr.service.okr.project.domain.enums;

import java.time.LocalDate;
import java.util.Map;

public enum SearchRange {
	WEEK("WEEK"),
	MONTH("MONTH"),
	HALF_YEAR("HALF_YEAR"),
	ALL("ALL");

	private final String code;

	SearchRange(final String code) {
		this.code = code;
	}

	public Map<String, LocalDate> getRange() {
		LocalDate now = LocalDate.now();

		switch (code) {
			case "WEEK":
				return Map.of("startDt", now.minusDays(7), "endDt", now.plusDays(1));
			case "MONTH":
				return Map.of("startDt", now.minusDays(30), "endDt", now.plusDays(1));
			case "HALF_YEAR":
				return Map.of("startDt", now.minusDays(183), "endDt", now.plusDays(1));
			default:
				return null;
		}
	}
}
