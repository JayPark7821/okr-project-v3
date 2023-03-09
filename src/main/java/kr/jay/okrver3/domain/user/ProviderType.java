package kr.jay.okrver3.domain.user;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.utils.EnumLookUpUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProviderType {
	APPLE("APPLE"),
	GOOGLE("GOOGLE");

	private final String name;

	public static ProviderType of(String code) {
		return EnumLookUpUtil.lookup(ProviderType.class, code, ErrorCode.UNSUPPORTED_SOCIAL_LOGIN);
	}

}
