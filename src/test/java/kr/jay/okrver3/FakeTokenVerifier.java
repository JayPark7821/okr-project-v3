package kr.jay.okrver3;

import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.infrastructure.user.auth.OAuth2UserInfo;
import kr.jay.okrver3.infrastructure.user.auth.TokenVerifier;

public class FakeTokenVerifier implements TokenVerifier {

	@Override
	public boolean support(ProviderType providerType) {
		return true;
	}

	@Override
	public OAuth2UserInfo verifyIdToken(String token) {
		if (token.equals("appleToken")) {
			return OAuth2UserInfoFixture.AppleUserInfoFixture.build();
		}
		return OAuth2UserInfoFixture.GoogleUserInfoFixture.build();

	}
}
