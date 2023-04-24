package kr.service.okr.user.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

public interface UserApiController {

	@PostMapping("/api/v1/user/login/{provider}/{idToken}")
	ResponseEntity<LoginResponse> loginWithIdToken(
		final @PathVariable("provider") String provider,
		final @PathVariable("idToken") String idToken
	);

	@PostMapping("/api/v1/user/join")
	ResponseEntity<LoginResponse> join(
		@RequestBody @Valid JoinRequest joinRequestDto
	);
}
