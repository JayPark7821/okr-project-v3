package kr.service.oauth.processor;

import kr.service.oauth.platform.OAuth2UserInfo;
import kr.service.oauth.platform.SocialPlatform;

public interface SocialTokenVerifier {

	boolean support(SocialPlatform socialPlatform);

	OAuth2UserInfo verifyIdToken(String token);
}
