package kr.service.user;

import kr.service.okr.util.EnumLookUpUtil;
import kr.service.user.exception.ErrorCode;

public enum ProviderType {
	APPLE,
	GOOGLE;

	public static ProviderType of(String provider) {
		return EnumLookUpUtil.lookup(ProviderType.class, provider, ErrorCode.UNSUPPORTED_SOCIAL_TYPE.getMessage());
	}

}
