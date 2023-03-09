package kr.jay.okrver3.domain.project;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.utils.EnumLookUpUtil;

public enum SortType {

	RECENTLY_CREATE,
	DEADLINE_CLOSE,
	PROGRESS_HIGH,
	PROGRESS_LOW;

	public static SortType of(String code) {
		return EnumLookUpUtil.lookup(SortType.class, code, ErrorCode.INVALID_SORT_TYPE);
	}

}
