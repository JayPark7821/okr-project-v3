package kr.service.okr.domain.user.auth;

import kr.service.okr.domain.user.ProviderType;
import kr.service.okr.infrastructure.user.auth.OAuth2UserInfo;

public interface TokenVerifyProcessor {
	OAuth2UserInfo verifyIdToken(ProviderType provider, String token);
}

