package kr.service.user.utils;

import kr.service.oauth.platform.OAuth2UserInfo;
import kr.service.oauth.processor.SocialTokenVerifier;
import kr.service.oauth.processor.SocialTokenVerifyProcessor;

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
