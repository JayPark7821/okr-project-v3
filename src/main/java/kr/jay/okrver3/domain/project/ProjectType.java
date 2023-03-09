package kr.jay.okrver3.domain.project;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.utils.EnumLookUpUtil;

public enum ProjectType {

	TEAM,
	SINGLE,
	ALL,
	;

	public static ProjectType of(String code) {
		return EnumLookUpUtil.lookup(ProjectType.class, code, ErrorCode.INVALID_PROJECT_TYPE);
	}

}
