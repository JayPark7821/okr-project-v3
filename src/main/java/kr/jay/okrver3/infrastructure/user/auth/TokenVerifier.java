package kr.jay.okrver3.infrastructure.user.auth;

import kr.jay.okrver3.domain.user.ProviderType;

public interface TokenVerifier {

	boolean support(ProviderType providerType);

	OAuth2UserInfo verifyIdToken(String token);
}
