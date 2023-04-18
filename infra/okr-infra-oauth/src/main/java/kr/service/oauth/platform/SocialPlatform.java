package kr.service.oauth.platform;

import kr.service.okr.util.EnumLookUpUtil;
import kr.service.user.exception.ErrorCode;

public enum SocialPlatform {
	GOOGLE,
	APPLE,
	;

	public static SocialPlatform of(String provider) {
		return EnumLookUpUtil.lookup(SocialPlatform.class, provider, ErrorCode.UNSUPPORTED_SOCIAL_TYPE.getMessage());
	}
}
