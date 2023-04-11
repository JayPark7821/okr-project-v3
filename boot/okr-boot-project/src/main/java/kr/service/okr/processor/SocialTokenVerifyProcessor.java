package kr.service.okr.processor;

import kr.service.oauth.OAuth2UserInfo;

public interface SocialTokenVerifyProcessor {
	OAuth2UserInfo verifyIdToken(String provider, String token);
}

