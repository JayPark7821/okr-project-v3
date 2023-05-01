package kr.service.okr.project.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import kr.service.okr.AuthenticationInfo;

public interface InitiativeApiController {

	@PostMapping("/api/v1/initiative")
	ResponseEntity<String> registerInitiative(
		final @RequestBody RegisterInitiativeRequest requestDto,
		final AuthenticationInfo authenticationInfo
	);
}
