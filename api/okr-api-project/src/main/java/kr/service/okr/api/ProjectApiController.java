package kr.service.okr.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "okr-api-project")
public interface ProjectApiController {

	@PostMapping("/api/v1/project")
	ResponseEntity<String> registerProject(RegisterProjectRequestDto request);

}
