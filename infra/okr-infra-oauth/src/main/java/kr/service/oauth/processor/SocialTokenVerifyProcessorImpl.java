package kr.service.oauth.processor;

import java.util.List;

import org.springframework.stereotype.Component;

import kr.service.oauth.platform.OAuth2UserInfo;
import kr.service.oauth.platform.SocialPlatform;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SocialTokenVerifyProcessorImpl implements SocialTokenVerifyProcessor {

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



