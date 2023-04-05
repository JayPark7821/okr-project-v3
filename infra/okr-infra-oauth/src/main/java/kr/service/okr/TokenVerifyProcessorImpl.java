package kr.service.okr;

import java.util.List;

import org.springframework.stereotype.Component;

import kr.service.okr.model.guset.ProviderType;
import kr.service.okr.model.user.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenVerifyProcessorImpl {

	private final List<TokenVerifier> tokenVerifierList;

	public OAuth2UserInfo verifyIdToken(ProviderType provider, String token) {
		TokenVerifier tokenVerifier = routingVerifierCaller(provider);
		return tokenVerifier.verifyIdToken(token);
	}

	private TokenVerifier routingVerifierCaller(ProviderType provider) {
		return tokenVerifierList.stream()
			.filter(tokenVerifier -> tokenVerifier.support(provider))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unsupported provider type"));
	}

}
