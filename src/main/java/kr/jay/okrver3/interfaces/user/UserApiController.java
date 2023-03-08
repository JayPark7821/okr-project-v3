package kr.jay.okrver3.interfaces.user;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.jay.okrver3.application.user.UserFacade;
import kr.jay.okrver3.common.Response;
import kr.jay.okrver3.common.utils.HeaderUtil;
import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.auth.TokenVerifyProcessor;
import kr.jay.okrver3.domain.user.service.LoginInfo;
import kr.jay.okrver3.infrastructure.user.auth.OAuth2UserInfo;
import kr.jay.okrver3.interfaces.user.request.JoinRequest;
import kr.jay.okrver3.interfaces.user.response.LoginResponse;
import kr.jay.okrver3.interfaces.user.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserApiController {

	private final TokenVerifyProcessor tokenVerifyProcessor;
	private final UserFacade userFacade;
	private final UserDtoMapper mapper;

	@PostMapping("/login/{provider}/{idToken}")
	ResponseEntity<LoginResponse> loginWithIdToken(
		@PathVariable("provider") String provider,
		@PathVariable("idToken") String idToken
	) {

		OAuth2UserInfo oAuth2UserInfo = tokenVerifyProcessor.verifyIdToken(ProviderType.of(provider), idToken);
		Optional<LoginInfo> loginInfo = userFacade.getLoginInfoFrom(oAuth2UserInfo);

		return loginInfo.map(this::getLoginResponseFrom)
			.orElseGet(() -> getLoginResponseFrom(userFacade.createGuestInfoFrom(oAuth2UserInfo)));
	}

	@PostMapping("/join")
	public ResponseEntity<LoginResponse> join(
		@RequestBody @Valid JoinRequest joinRequestDto
	) {

		return Response.successCreated(
			mapper.of(userFacade.join(joinRequestDto))
		);
	}

	@GetMapping("/refresh")
	public ResponseEntity<TokenResponse> getNewAccessToken(HttpServletRequest request) {

		return Response.successOk(
			mapper.of(userFacade.getNewAccessToken(HeaderUtil.getAccessToken(request)))
		);
	}

	private ResponseEntity<LoginResponse> getLoginResponseFrom(LoginInfo loginInfo) {
		return Response.successOk(
			mapper.of(loginInfo)
		);
	}

}
