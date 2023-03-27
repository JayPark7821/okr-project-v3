package kr.service.okr.infrastructure.user.auth;

import java.util.List;

import org.springframework.stereotype.Component;

import kr.service.okr.domain.user.ProviderType;
import kr.service.okr.domain.user.auth.TokenVerifyProcessor;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenVerifyProcessorImpl implements TokenVerifyProcessor {

	private final List<TokenVerifier> tokenVerifierList;

	@Override
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
