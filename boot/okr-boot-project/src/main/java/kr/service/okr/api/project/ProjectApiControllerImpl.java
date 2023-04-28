package kr.service.okr.api.project;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.service.okr.AuthenticationInfo;
import kr.service.okr.api.Response;
import kr.service.okr.application.project.ProjectFacade;
import kr.service.okr.common.security.core.context.AuthenticatedUser;
import kr.service.okr.project.api.ProjectApiController;
import kr.service.okr.project.api.ProjectInfoResponse;
import kr.service.okr.project.api.RegisterProjectRequestDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectApiControllerImpl implements ProjectApiController {

	private final ProjectFacade projectFacade;

	@Override
	@PostMapping
	public ResponseEntity<String> registerProject(
		final @RequestBody @Valid RegisterProjectRequestDto request,
		final @AuthenticatedUser AuthenticationInfo authenticationInfo
	) {
		return Response.successCreated(
			projectFacade.registerProject(ProjectDtoMapper.toCommand(request, authenticationInfo.userSeq()))
		);
	}

	@Override
	@GetMapping("/{projectToken}")
	public ResponseEntity<ProjectInfoResponse> getProjectInfoBy(
		final @PathVariable("projectToken") String projectToken,
		final @AuthenticatedUser AuthenticationInfo authenticationInfo
	) {
		return Response.successOk(
			ProjectDtoMapper.of(projectFacade.getProjectInfoBy(projectToken, authenticationInfo.userSeq()))
		);
	}
}
