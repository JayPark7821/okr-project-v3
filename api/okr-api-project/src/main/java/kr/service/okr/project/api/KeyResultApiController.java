package kr.service.okr.project.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import kr.service.okr.AuthenticationInfo;

public interface KeyResultApiController {

	@PostMapping("/api/v1/keyresult")
	ResponseEntity<String> registerKeyResult(
		final @RequestBody RegisterKeyResultRequest request,
		final AuthenticationInfo authenticationInfo
	);

	@PutMapping("/api/v1/keyresult")
	ResponseEntity<String> updateKeyResult(
		final @RequestBody UpdateKeyResultRequest request,
		final AuthenticationInfo authenticationInfo
	);
}
