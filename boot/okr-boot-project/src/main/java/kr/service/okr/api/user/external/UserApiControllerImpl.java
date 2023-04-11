package kr.service.okr.api.user.external;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.service.oauth.OAuth2UserInfo;
import kr.service.oauth.SocialTokenVerifyProcessor;
import kr.service.user.api.LoginResponse;
import kr.service.user.api.UserApiController;
import kr.service.user.guest.ProviderType;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserApiControllerImpl implements UserApiController {

	private final SocialTokenVerifyProcessor socialTokenVerifyProcessor;

	@Override
	@PostMapping("/login/{provider}/{idToken}")
	public ResponseEntity<LoginResponse> loginWithIdToken(final String provider, final String idToken) {

		OAuth2UserInfo oAuth2UserInfo =
			socialTokenVerifyProcessor.verifyIdToken(ProviderType.of(provider).name(), idToken);

		return null;
	}
}
