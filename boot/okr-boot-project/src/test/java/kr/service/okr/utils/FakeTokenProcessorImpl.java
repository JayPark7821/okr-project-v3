package kr.service.okr.utils;

import kr.service.oauth.platform.OAuth2UserInfo;
import kr.service.oauth.processor.SocialTokenVerifier;
import kr.service.oauth.processor.SocialTokenVerifyProcessor;

public class FakeTokenProcessorImpl implements SocialTokenVerifyProcessor {

	private final SocialTokenVerifier tokenVerifier;

	public FakeTokenProcessorImpl(SocialTokenVerifier tokenVerifier) {
		this.tokenVerifier = tokenVerifier;
	}

	@Override
	public OAuth2UserInfo verifyIdToken(final String provider, final String token) {
		return tokenVerifier.verifyIdToken(token);
	}
}
