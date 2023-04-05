package kr.service.okr.user.service;

import kr.service.okr.model.guset.ProviderType;
import kr.service.okr.model.user.OAuth2UserInfo;

public interface SocialTokenVerifyProcessor {
	OAuth2UserInfo verifyIdToken(ProviderType provider, String token);
}

