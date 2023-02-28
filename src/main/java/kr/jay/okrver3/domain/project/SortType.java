package kr.jay.okrver3.domain.project;

import java.util.Arrays;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;

public enum SortType {

	RECENTLY_CREATE,
	DEADLINE_CLOSE,
	PROGRESS_HIGH,
	PROGRESS_LOW;

	public static SortType of(String sortTypeString) {
		return Arrays.stream(SortType.values())
			.filter(sortType -> sortType.name().equals(sortTypeString))
			.findAny()
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_SORT_TYPE));
	}

}
