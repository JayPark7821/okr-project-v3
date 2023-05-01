package kr.service.okr.api.project;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.service.okr.AuthenticationInfo;
import kr.service.okr.api.Response;
import kr.service.okr.application.project.InitiativeFacade;
import kr.service.okr.common.security.core.context.AuthenticatedUser;
import kr.service.okr.project.api.InitiativeApiController;
import kr.service.okr.project.api.RegisterInitiativeRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/initiative")
@RequiredArgsConstructor
public class InitiativeApiControllerImpl implements InitiativeApiController {

	private final InitiativeFacade initiativeFacade;

	@Override
	@PostMapping
	public ResponseEntity<String> registerInitiative(
		final @RequestBody RegisterInitiativeRequest requestDto,
		final @AuthenticatedUser AuthenticationInfo authenticationInfo
	) {

		return Response.successCreated(
			initiativeFacade.registerInitiative(
				InitiativeDtoMapper.of(requestDto),
				authenticationInfo.userSeq()
			)
		);
	}
}
