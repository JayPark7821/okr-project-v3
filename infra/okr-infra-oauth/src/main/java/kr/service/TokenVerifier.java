package kr.service;

import kr.service.model.guset.ProviderType;
import kr.service.model.user.OAuth2UserInfo;

public interface TokenVerifier {

	boolean support(ProviderType providerType);

	OAuth2UserInfo verifyIdToken(String token);
}
