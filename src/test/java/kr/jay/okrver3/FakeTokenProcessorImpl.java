package kr.jay.okrver3;

import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.auth.TokenVerifyProcessor;
import kr.jay.okrver3.infrastructure.user.auth.OAuth2UserInfo;
import kr.jay.okrver3.infrastructure.user.auth.TokenVerifier;

public class FakeTokenProcessorImpl implements TokenVerifyProcessor {

	private final TokenVerifier tokenVerifier;

	public FakeTokenProcessorImpl(TokenVerifier tokenVerifier) {
		this.tokenVerifier = tokenVerifier;
	}

	@Override
	public OAuth2UserInfo verifyIdToken(ProviderType provider, String token) {
		return tokenVerifier.verifyIdToken(token);
	}

}
