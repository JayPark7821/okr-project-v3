package kr.jay.okrver3.interfaces.user;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.jay.okrver3.application.user.LoginInfo;
import kr.jay.okrver3.application.user.UserFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserApiController {

	private final TokenVerifier tokenVerifier;
	private final UserFacade userFacade;


	@PostMapping("/login/{provider}/{idToken}")
	ResponseEntity<LoginResponse> loginWithIdToken(
		@PathVariable("provider") String provider,
		@PathVariable("idToken") String idToken
	) {

		OAuth2UserInfo oAuth2UserInfo = tokenVerifier.verifyIdToken(idToken);
		LoginInfo loginInfo = userFacade.getLoginInfoFrom(oAuth2UserInfo);

		return ResponseEntity.status(HttpStatus.OK)
			.body(new LoginResponse(loginInfo));
	}
}
