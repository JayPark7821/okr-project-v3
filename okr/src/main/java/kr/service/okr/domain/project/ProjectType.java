package kr.service.okr.domain.project;

import kr.service.okrcommon.common.exception.ErrorCode;
import kr.service.okrcommon.common.utils.EnumLookUpUtil;

public enum ProjectType {

	TEAM,
	SINGLE,
	ALL,
	;

	public static ProjectType of(String code) {
		return EnumLookUpUtil.lookup(ProjectType.class, code, ErrorCode.INVALID_PROJECT_TYPE);
	}

}
