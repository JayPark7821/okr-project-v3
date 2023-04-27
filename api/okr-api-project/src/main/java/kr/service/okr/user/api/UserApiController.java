package kr.service.okr.user.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import kr.service.okr.AuthenticationInfo;

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

	@GetMapping("/api/v1/user/job/category")
	ResponseEntity<List<JobResponse>> getJobCategory();

	@GetMapping("/api/v1/user/job/{category}/fields")
	ResponseEntity<List<JobResponse>> getJobField(
		@PathVariable("category") String category
	);

	@GetMapping("/api/v1/user/refresh")
	ResponseEntity<TokenResponse> getNewAccessToken(
		AuthenticationInfo authenticationInfo
	);
}
