package kr.jay.okrver3.domain.project;

import java.util.Arrays;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.user.ProviderType;

public enum ProjectType {

	TEAM,
	SINGLE,
	ALL,
	;


	public static ProviderType of(String providerInString) {
		return Arrays.stream(ProviderType.values())
			.filter(provider -> provider.getName().equals(providerInString))
			.findAny()
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_PROJECT_TYPE));
	}

}
