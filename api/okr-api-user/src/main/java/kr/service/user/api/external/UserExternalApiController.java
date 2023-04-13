package kr.service.user.api.external;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import kr.service.user.api.LoginResponse;

public interface UserExternalApiController {

	@PostMapping("/api/v1/user/login/{provider}/{idToken}")
	ResponseEntity<LoginResponse> loginWithIdToken(
		final @PathVariable("provider") String provider,
		final @PathVariable("idToken") String idToken
	);
}
