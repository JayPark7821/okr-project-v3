package kr.service.okr.api.project;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.service.okr.application.ProjectFacade;
import kr.service.okr.web.Response;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectApiController {

	private final ProjectFacade projectFacade;

	@PostMapping
	ResponseEntity<String> registerProject(
		@RequestBody @Valid RegisterProjectRequestDto request
	) {

		return Response.successCreated(
			projectFacade.registerProject(ProjectMapper.toCommand(request, 1L))
		);
	}

}
