package kr.service.user.service;

import kr.service.model.guset.ProviderType;
import kr.service.model.user.OAuth2UserInfo;

public interface SocialTokenVerifyProcessor {
	OAuth2UserInfo verifyIdToken(ProviderType provider, String token);
}

