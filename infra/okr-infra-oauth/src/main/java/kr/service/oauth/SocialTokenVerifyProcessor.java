package kr.service.oauth;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SocialTokenVerifyProcessor {

	private final List<SocialTokenVerifier> socialTokenVerifierList;

	public OAuth2UserInfo verifyIdToken(String socialPlatform, String token) {
		SocialTokenVerifier socialTokenVerifier = routingVerifierCaller(SocialPlatform.of(socialPlatform));
		return socialTokenVerifier.verifyIdToken(token);
	}

	private SocialTokenVerifier routingVerifierCaller(SocialPlatform socialPlatform) {
		return socialTokenVerifierList.stream()
			.filter(tokenVerifier -> tokenVerifier.support(socialPlatform))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unsupported provider type"));
	}
}



