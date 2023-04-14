package kr.service.user.api.user.internal;

import static kr.service.user.api.user.Mapper.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.service.user.Response;
import kr.service.user.api.internal.UserInfoResponse;
import kr.service.user.api.internal.UserInternalApiController;
import kr.service.user.application.user.UserFacade;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserInternalApiControllerImpl implements UserInternalApiController {

	private final UserFacade userFacade;

	@Override
	@GetMapping("/auth")
	public ResponseEntity<UserInfoResponse> getUserInfoBy(final String jwt) {
		return Response.successOk(of(userFacade.getUserInfoBy(jwt)));
	}

}
