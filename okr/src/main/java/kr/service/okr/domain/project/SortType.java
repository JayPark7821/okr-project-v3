package kr.service.okr.domain.project;

import kr.service.okrcommon.common.exception.ErrorCode;
import kr.service.okrcommon.common.utils.EnumLookUpUtil;

public enum SortType {

	RECENTLY_CREATE,
	DEADLINE_CLOSE,
	PROGRESS_HIGH,
	PROGRESS_LOW;

	public static SortType of(String code) {
		return EnumLookUpUtil.lookup(SortType.class, code, ErrorCode.INVALID_SORT_TYPE);
	}

}
