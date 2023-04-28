package kr.service.okr.project.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import kr.service.okr.AuthenticationInfo;

public interface KeyResultApiController {

	@PostMapping("/api/v1/project/keyresult")
	ResponseEntity<String> registerKeyResult(
		final @RequestBody RegisterKeyResultRequest request,
		final AuthenticationInfo authenticationInfo
	);
}
