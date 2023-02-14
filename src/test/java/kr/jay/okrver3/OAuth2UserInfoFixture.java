package kr.jay.okrver3;

import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.interfaces.user.auth.OAuth2UserInfo;

public final class OAuth2UserInfoFixture {

	public static final class GoogleUserInfoFixture {
		public static final String ID = "fakeGoogleId";
		public static final String NAME = "fakeGoogleName";
		public static final String EMAIL = "fakeGoogleIdEmail";
		public static final String PIC = "fakeGoogleIdPic";
		public static final ProviderType PROVIDER_TYPE = ProviderType.GOOGLE;

		public static OAuth2UserInfo build() {
			return new OAuth2UserInfo(ID, NAME, EMAIL, PIC, PROVIDER_TYPE);
		}
	}

	public static final class AppleUserInfoFixture {
		public static final String ID = "fakeAppleId";
		public static final String NAME = "fakeAppleName";
		public static final String EMAIL = "fakeAppleEmail";
		public static final String PIC = "fakeApplePic";
		public static final ProviderType PROVIDER_TYPE = ProviderType.APPLE;

		public static OAuth2UserInfo build() {
			return new OAuth2UserInfo(ID, NAME, EMAIL, PIC, PROVIDER_TYPE);
		}
	}
}
