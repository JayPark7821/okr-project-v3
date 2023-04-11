package kr.service.okr.api.user.external;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.service.okr.application.user.SocialTokenVerifyProcessor;
import kr.service.okr.exception.ErrorCode;
import kr.service.okr.model.guset.ProviderType;
import kr.service.okr.model.user.OAuth2UserInfo;
import kr.service.okr.util.EnumLookUpUtil;
import kr.service.user.api.LoginResponse;
import kr.service.user.api.UserApiController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserApiControllerImpl implements UserApiController {

	private final SocialTokenVerifyProcessor socialTokenVerifyProcessor;

	@Override
	@PostMapping("/login/{provider}/{idToken}")
	public ResponseEntity<LoginResponse> loginWithIdToken(final String provider, final String idToken) {
		//TODO
		final ProviderType providerType =
			EnumLookUpUtil.lookup(ProviderType.class, provider, ErrorCode.UNSUPPORTED_SOCIAL_TYPE);

		OAuth2UserInfo oAuth2UserInfo =
			socialTokenVerifyProcessor.verifyIdToken(providerType, idToken);
		return null;
	}
}
