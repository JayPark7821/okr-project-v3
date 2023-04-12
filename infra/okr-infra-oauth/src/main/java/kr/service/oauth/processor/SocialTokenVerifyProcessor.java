package kr.service.oauth.processor;

import kr.service.oauth.platform.OAuth2UserInfo;

public interface SocialTokenVerifyProcessor {
	OAuth2UserInfo verifyIdToken(String provider, String token);
}

