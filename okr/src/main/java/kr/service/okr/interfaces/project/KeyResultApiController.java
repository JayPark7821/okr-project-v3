package kr.service.okr.interfaces.project;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import kr.service.okr.application.project.ProjectFacade;
import kr.service.okrcommon.common.Response;
import kr.service.okr.interfaces.AbstractController;
import kr.service.okr.interfaces.project.request.ProjectKeyResultSaveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/keyresult")
public class KeyResultApiController extends AbstractController {

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
				getUserSeqFromAuthentication(authentication)
			)
		);
	}

}