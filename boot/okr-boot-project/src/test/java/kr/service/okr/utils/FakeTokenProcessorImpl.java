package kr.service.okr.utils;

import kr.service.oauth.OAuth2UserInfo;
import kr.service.oauth.SocialTokenVerifier;
import kr.service.okr.processor.SocialTokenVerifyProcessor;

public class FakeTokenProcessorImpl implements SocialTokenVerifyProcessor {

	private final SocialTokenVerifier socialTokenVerifier;

	public FakeTokenProcessorImpl(SocialTokenVerifier socialTokenVerifier) {
		this.socialTokenVerifier = socialTokenVerifier;
	}

	@Override
	public OAuth2UserInfo verifyIdToken(String provider, String token) {
		return socialTokenVerifier.verifyIdToken(token);
	}

}
