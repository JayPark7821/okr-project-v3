package kr.service.okr.utils;

import kr.service.oauth.platform.OAuth2UserInfo;
import kr.service.oauth.platform.SocialPlatform;
import kr.service.oauth.processor.SocialTokenVerifier;

public class FakeTokenVerifier implements SocialTokenVerifier {

	@Override
	public boolean support(final SocialPlatform socialPlatform) {
		return true;
	}

	@Override
	public OAuth2UserInfo verifyIdToken(String token) {
		if (token.equals("memberAppleIdToken")) {
			return OAuth2UserInfoFixture.MemberAppleInfoFixture.build();
		} else if (token.equals("notMemberAppleIdToken")) {
			return OAuth2UserInfoFixture.NotMemberAppleInfoFixture.build();
		}
		return OAuth2UserInfoFixture.DiffAppleUserInfoFixture.build();

	}
}
