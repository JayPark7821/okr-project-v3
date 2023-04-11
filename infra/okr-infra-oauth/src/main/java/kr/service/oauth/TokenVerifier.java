package kr.service.oauth;

import kr.service.okr.model.guset.ProviderType;
import kr.service.okr.model.user.OAuth2UserInfo;

public interface TokenVerifier {

	boolean support(ProviderType providerType);

	OAuth2UserInfo verifyIdToken(String token);
}
