package kr.service.okr.project.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import kr.service.okr.AuthenticationInfo;

public interface ProjectApiController {

	@PostMapping("/api/v1/project")
	ResponseEntity<String> registerProject(
		final @RequestBody RegisterProjectRequest request,
		final AuthenticationInfo authenticationInfo
	);

	@GetMapping("/api/v1/project/{projectToken}")
	ResponseEntity<ProjectInfoResponse> getProjectInfoBy(
		final @PathVariable("projectToken") String projectToken,
		final AuthenticationInfo authenticationInfo
	);

}
