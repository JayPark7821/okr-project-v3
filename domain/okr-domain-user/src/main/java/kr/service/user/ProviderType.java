package kr.service.user;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.util.EnumLookUpUtil;

public enum ProviderType {
	APPLE,
	GOOGLE;

	public static ProviderType of(String provider) {
		return EnumLookUpUtil.lookup(ProviderType.class, provider, ErrorCode.UNSUPPORTED_SOCIAL_TYPE);
	}

}
