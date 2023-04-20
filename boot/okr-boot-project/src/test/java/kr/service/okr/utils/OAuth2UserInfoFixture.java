package kr.service.okr.utils;

import kr.service.oauth.platform.OAuth2UserInfo;
import kr.service.oauth.platform.SocialPlatform;

public final class OAuth2UserInfoFixture {

	public static final class DiffAppleUserInfoFixture {
		public static final String ID = "fakeGoogleId";
		public static final String NAME = "fakeGoogleName";
		public static final String EMAIL = "fakeGoogleIdEmail";
		public static final String PIC = "fakeGoogleIdPic";
		public static final SocialPlatform PROVIDER_TYPE = SocialPlatform.APPLE;

		public static OAuth2UserInfo build() {
			return new OAuth2UserInfo(ID, NAME, EMAIL, PIC, PROVIDER_TYPE.name());
		}
	}

	public static final class AppleUserInfoFixture {
		public static final String ID = "fakeAppleId";
		public static final String NAME = "fakeAppleName";
		public static final String EMAIL = "fakeAppleEmail";
		public static final String PIC = "fakeApplePic";
		public static final SocialPlatform PROVIDER_TYPE = SocialPlatform.APPLE;

		public static OAuth2UserInfo build() {
			return new OAuth2UserInfo(ID, NAME, EMAIL, PIC, PROVIDER_TYPE.name());
		}
	}

	public static final class NotMemberInfoFixture {
		public static final String ID = "notMemberId";
		public static final String NAME = "notMemberName";
		public static final String EMAIL = "notMemberEmail@email.com";
		public static final String PIC = "notMemberPic";
		public static final SocialPlatform PROVIDER_TYPE = SocialPlatform.APPLE;

		public static OAuth2UserInfo build() {
			return new OAuth2UserInfo(ID, NAME, EMAIL, PIC, PROVIDER_TYPE.name());
		}
	}
}
