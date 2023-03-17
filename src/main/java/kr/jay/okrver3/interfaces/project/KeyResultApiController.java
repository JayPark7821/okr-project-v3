package kr.jay.okrver3.interfaces.project;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.jay.okrver3.application.project.ProjectFacade;
import kr.jay.okrver3.common.Response;
import kr.jay.okrver3.interfaces.project.request.ProjectKeyResultSaveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/keyresult")
public class KeyResultApiController extends AbstractProjectController {

	private final ProjectFacade projectFacade;
	private final ProjectDtoMapper mapper;

	@PostMapping
	ResponseEntity<String> registerKeyResult(
		@RequestBody @Valid ProjectKeyResultSaveRequest requestDto,
		Authentication authentication
	) {

		return Response.successCreated(
			projectFacade.registerKeyResult(
				mapper.of(requestDto),
				getUserFromAuthentication(authentication)
			)
		);
	}

}