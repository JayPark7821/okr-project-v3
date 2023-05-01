package kr.service.okr.api.project;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.service.okr.AuthenticationInfo;
import kr.service.okr.project.api.InitiativeApiController;
import kr.service.okr.project.api.RegisterInitiativeRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/initiative")
@RequiredArgsConstructor
public class InitiativeApiControllerImpl implements InitiativeApiController {

	@Override
	public ResponseEntity<String> registerInitiative(final RegisterInitiativeRequest requestDto,
		final AuthenticationInfo authenticationInfo) {
		return null;
	}
}
