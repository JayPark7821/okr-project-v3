package kr.jay.okrver3.interfaces.user;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.jay.okrver3.application.user.LoginInfo;
import kr.jay.okrver3.application.user.UserFacade;
import kr.jay.okrver3.common.Response;
import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.auth.TokenVerifyProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserApiController {

	private final TokenVerifyProcessor tokenVerifyProcessor;
	private final UserFacade userFacade;

	@PostMapping("/login/{provider}/{idToken}")
	ResponseEntity<LoginResponse> loginWithIdToken(
		@PathVariable("provider") String provider,
		@PathVariable("idToken") String idToken
	) {

		Optional<LoginInfo> loginInfo = userFacade.getLoginInfoFrom(
			tokenVerifyProcessor.verifyIdToken(ProviderType.of(provider), idToken)
		);

		return loginInfo.map(this::getLoginResponseFrom)
			.orElseGet(() -> getLoginResponseFrom(userFacade.createGuestInfoFrom(
				tokenVerifyProcessor.verifyIdToken(ProviderType.of(provider), idToken))));
	}

	@PostMapping("/join")
	public ResponseEntity<LoginResponse> join(
		@RequestBody @Valid JoinRequestDto joinRequestDto
	) {

		return Response.success(
			HttpStatus.CREATED,
			new LoginResponse(userFacade.join(joinRequestDto))
		);
	}

	private ResponseEntity<LoginResponse> getLoginResponseFrom(LoginInfo loginInfo) {
		return Response.success(
			HttpStatus.OK,
			new LoginResponse(loginInfo)
		);
	}
}
