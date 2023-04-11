package kr.service.user.guest;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.util.EnumLookUpUtil;

public enum ProviderType {
	APPLE("APPLE"),
	GOOGLE("GOOGLE");

	private final String name;

	ProviderType(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static ProviderType of(String provider) {
		return EnumLookUpUtil.lookup(ProviderType.class, provider, ErrorCode.UNSUPPORTED_SOCIAL_TYPE);
	}

}
