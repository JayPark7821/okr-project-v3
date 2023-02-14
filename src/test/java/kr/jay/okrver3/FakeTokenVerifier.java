package kr.jay.okrver3;

import kr.jay.okrver3.interfaces.user.auth.OAuth2UserInfo;
import kr.jay.okrver3.interfaces.user.auth.TokenVerifier;

public class FakeTokenVerifier implements TokenVerifier {

	@Override
	public OAuth2UserInfo verifyIdToken(String token) {
		if (token.equals("appleToken")) {
			return OAuth2UserInfoFixture.AppleUserInfoFixture.build();
		}
		return OAuth2UserInfoFixture.GoogleUserInfoFixture.build();

	}
}
