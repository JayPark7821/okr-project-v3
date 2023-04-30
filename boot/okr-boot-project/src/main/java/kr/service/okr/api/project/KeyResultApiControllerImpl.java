package kr.service.okr.api.project;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.service.okr.AuthenticationInfo;
import kr.service.okr.api.Response;
import kr.service.okr.application.project.KeyResultFacade;
import kr.service.okr.common.security.core.context.AuthenticatedUser;
import kr.service.okr.project.api.KeyResultApiController;
import kr.service.okr.project.api.RegisterKeyResultRequest;
import kr.service.okr.project.api.UpdateKeyResultRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/keyresult")
@RequiredArgsConstructor
public class KeyResultApiControllerImpl implements KeyResultApiController {

	private final KeyResultFacade keyResultFacade;

	@Override
	@PostMapping
	public ResponseEntity<String> registerKeyResult(
		final @RequestBody RegisterKeyResultRequest request,
		final @AuthenticatedUser AuthenticationInfo authenticationInfo
	) {
		return Response.successCreated(
			keyResultFacade.registerKeyResult(KeyResultDtoMapper.toCommand(request), authenticationInfo.userSeq())
		);
	}

	@Override
	@PutMapping
	public ResponseEntity<String> updateKeyResult(
		final @RequestBody UpdateKeyResultRequest request,
		final @AuthenticatedUser AuthenticationInfo authenticationInfo
	) {
		return null;
	}
}
