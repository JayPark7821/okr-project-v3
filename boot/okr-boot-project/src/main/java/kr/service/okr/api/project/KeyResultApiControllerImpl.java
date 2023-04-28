package kr.service.okr.api.project;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.service.okr.AuthenticationInfo;
import kr.service.okr.api.Response;
import kr.service.okr.application.project.KeyResultFacade;
import kr.service.okr.project.api.KeyResultApiController;
import kr.service.okr.project.api.RegisterKeyResultRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/keyresult")
@RequiredArgsConstructor
public class KeyResultApiControllerImpl implements KeyResultApiController {

	private final KeyResultFacade keyResultFacade;

	@Override
	public ResponseEntity<String> registerKeyResult(final RegisterKeyResultRequest request,
		final AuthenticationInfo authenticationInfo) {
		return Response.successCreated(
			keyResultFacade.registerKeyResult(KeyResultDtoMapper.toCommand(request), authenticationInfo.userSeq())
		);
	}
}
