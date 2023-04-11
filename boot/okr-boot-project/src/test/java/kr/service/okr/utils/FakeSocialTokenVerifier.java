package kr.service.okr.utils;

import kr.service.oauth.OAuth2UserInfo;
import kr.service.oauth.SocialPlatform;
import kr.service.oauth.SocialTokenVerifier;

public class FakeSocialTokenVerifier implements SocialTokenVerifier {

	@Override
	public boolean support(SocialPlatform providerType) {
		return true;
	}

	@Override
	public OAuth2UserInfo verifyIdToken(String token) {
		if (token.equals("appleToken")) {
			return OAuth2UserInfoFixture.AppleUserInfoFixture.build();
		} else if (token.equals("notMemberIdToken")) {
			return OAuth2UserInfoFixture.NotMemberInfoFixture.build();
		}
		return OAuth2UserInfoFixture.DiffAppleUserInfoFixture.build();

	}
}
