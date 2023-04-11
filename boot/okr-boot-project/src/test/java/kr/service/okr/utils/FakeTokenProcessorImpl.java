package kr.service.okr.utils;

import kr.service.oauth.TokenVerifier;
import kr.service.okr.application.user.SocialTokenVerifyProcessor;
import kr.service.okr.model.guset.ProviderType;
import kr.service.okr.model.user.OAuth2UserInfo;

public class FakeTokenProcessorImpl implements SocialTokenVerifyProcessor {

	private final TokenVerifier tokenVerifier;

	public FakeTokenProcessorImpl(TokenVerifier tokenVerifier) {
		this.tokenVerifier = tokenVerifier;
	}

	@Override
	public OAuth2UserInfo verifyIdToken(ProviderType provider, String token) {
		return tokenVerifier.verifyIdToken(token);
	}

}
