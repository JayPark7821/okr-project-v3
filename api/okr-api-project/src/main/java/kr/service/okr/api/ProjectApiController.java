package kr.service.okr.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

public interface ProjectApiController {

	@PostMapping("/api/v1/project")
	ResponseEntity<String> registerProject(final RegisterProjectRequestDto request);

	@GetMapping("/api/v1/project/{projectToken}")
	ResponseEntity<ProjectInfoResponse> getProjectInfoBy(
		final @PathVariable("projectToken") String projectToken,
		final @RequestHeader("Authorization") String authorization
	);

}
