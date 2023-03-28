package kr.service.okr.domain.user;

import kr.service.okrcommon.common.exception.ErrorCode;
import kr.service.okrcommon.common.utils.EnumLookUpUtil;
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
