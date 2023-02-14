package kr.jay.okrver3.domain.user.auth;

import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.infrastructure.user.auth.OAuth2UserInfo;

public interface TokenVerifyProcessor {
	OAuth2UserInfo verifyIdToken(ProviderType provider, String token);
}

