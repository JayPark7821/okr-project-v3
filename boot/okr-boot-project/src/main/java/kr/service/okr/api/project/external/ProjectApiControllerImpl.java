package kr.service.okr.api.project.external;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.service.okr.Response;
import kr.service.okr.api.ProjectApiController;
import kr.service.okr.api.RegisterProjectRequestDto;
import kr.service.okr.application.project.ProjectFacade;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectApiControllerImpl implements ProjectApiController {

	private final ProjectFacade projectFacade;

	@Override
	@PostMapping
	public ResponseEntity<String> registerProject(
		@RequestBody @Valid RegisterProjectRequestDto request
	) {

		return Response.successCreated(
			projectFacade.registerProject(ProjectMapper.toCommand(request, 1L))
		);
	}
}
