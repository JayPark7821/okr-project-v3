package kr.service.okr.api.project;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.service.okr.AuthenticationInfo;
import kr.service.okr.project.api.KeyResultApiController;
import kr.service.okr.project.api.RegisterKeyResultRequest;

@RestController
@RequestMapping("/api/v1/keyresult")
public class KeyResultApiControllerImpl implements KeyResultApiController {

	@Override
	public ResponseEntity<String> registerKeyResult(final RegisterKeyResultRequest request,
		final AuthenticationInfo authenticationInfo) {
		return null;
	}
}
