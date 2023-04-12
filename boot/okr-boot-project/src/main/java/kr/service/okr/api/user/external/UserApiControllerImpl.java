package kr.service.okr.api.user.external;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.service.oauth.platform.OAuth2UserInfo;
import kr.service.oauth.processor.SocialTokenVerifyProcessor;
import kr.service.okr.api.user.Mapper;
import kr.service.okr.application.user.LoginInfo;
import kr.service.okr.application.user.UserFacade;
import kr.service.user.ProviderType;
import kr.service.user.Response;
import kr.service.user.api.LoginResponse;
import kr.service.user.api.UserApiController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserApiControllerImpl implements UserApiController {

	private final SocialTokenVerifyProcessor socialTokenVerifyProcessor;
	private final UserFacade userFacade;

	@Override
	@PostMapping("/login/{provider}/{idToken}")
	public ResponseEntity<LoginResponse> loginWithIdToken(final String provider, final String idToken) {

		OAuth2UserInfo oAuth2UserInfo =
			socialTokenVerifyProcessor.verifyIdToken(ProviderType.of(provider).name(), idToken);
		Optional<LoginInfo> loginInfo = userFacade.getLoginInfoFrom(oAuth2UserInfo);

		return Response.successOk(loginInfo.map(Mapper::of)
			.orElseGet(() -> Mapper.of(userFacade.createGuest(oAuth2UserInfo))));
	}
}
