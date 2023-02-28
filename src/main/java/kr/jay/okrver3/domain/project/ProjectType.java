package kr.jay.okrver3.domain.project;

import java.util.Arrays;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;

public enum ProjectType {

	TEAM,
	SINGLE,
	ALL,
	;

	public static ProjectType of(String projectTypeString) {
		return Arrays.stream(ProjectType.values())
			.filter(projectType -> projectType.name().equals(projectTypeString))
			.findAny()
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_PROJECT_TYPE));
	}

}
