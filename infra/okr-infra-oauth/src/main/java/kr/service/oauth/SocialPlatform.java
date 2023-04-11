package kr.service.oauth;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.util.EnumLookUpUtil;

public enum SocialPlatform {
	GOOGLE,
	APPLE,
	;

	public static SocialPlatform of(String provider) {
		return EnumLookUpUtil.lookup(SocialPlatform.class, provider, ErrorCode.UNSUPPORTED_SOCIAL_TYPE);
	}
}
