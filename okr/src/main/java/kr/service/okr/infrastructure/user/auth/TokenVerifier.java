package kr.service.okr.infrastructure.user.auth;

import kr.service.okr.domain.user.ProviderType;

public interface TokenVerifier {

	boolean support(ProviderType providerType);

	OAuth2UserInfo verifyIdToken(String token);
}
