package kr.service.okr.utils;

import kr.service.oauth.TokenVerifier;
import kr.service.okr.model.guset.ProviderType;
import kr.service.okr.model.user.OAuth2UserInfo;

public class FakeTokenVerifier implements TokenVerifier {

	@Override
	public boolean support(ProviderType providerType) {
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
