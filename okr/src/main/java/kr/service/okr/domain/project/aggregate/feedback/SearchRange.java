package kr.service.okr.domain.project.aggregate.feedback;

import java.time.LocalDate;
import java.util.Map;

import kr.service.okrcommon.common.exception.ErrorCode;
import kr.service.okrcommon.common.utils.EnumLookUpUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchRange {
	WEEK("WEEK"),
	MONTH("MONTH"),
	HALF_YEAR("HALF_YEAR"),
	ALL("ALL");

	private final String code;

	public static SearchRange of(String code) {
		return EnumLookUpUtil.lookup(SearchRange.class, code, ErrorCode.INVALID_SEARCH_RANGE_TYPE);
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
