package kr.jay.okrver3.interfaces.initiative;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.jay.okrver3.application.initiative.InitiativeFacade;
import kr.jay.okrver3.common.Response;
import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.common.utils.ClassUtils;
import kr.jay.okrver3.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/initiative")
@RequiredArgsConstructor
public class InitiativeApiController {

	private final InitiativeFacade initiativeFacade;

	@PostMapping("/initiative")
	public ResponseEntity<String> registerInitiative(
		@RequestBody @Valid InitiativeSaveDto requestDto,
		Authentication authentication
	) {

		User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_USER_FAILED));

		return Response
			.success(
				HttpStatus.CREATED,
				initiativeFacade.registerInitiative(requestDto.toCommand(), user)
			);
	}
}
