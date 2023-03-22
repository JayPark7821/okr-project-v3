package kr.service.okr.util;

import kr.service.okr.domain.user.ProviderType;
import kr.service.okr.domain.user.auth.TokenVerifyProcessor;
import kr.service.okr.infrastructure.user.auth.OAuth2UserInfo;
import kr.service.okr.infrastructure.user.auth.TokenVerifier;

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
