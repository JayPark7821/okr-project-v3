package kr.service.okr.api.user.internal;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.service.okr.Response;
import kr.service.okr.api.user.Mapper;
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
	public ResponseEntity<UserInfoResponse> getUserInfoBy(
		final @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt
	) {
		return Response.successOk(Mapper.of(userFacade.getUserInfoBy(jwt)));
	}

	@Override
	@GetMapping("/validateEmails")
	public ResponseEntity<List<Long>> getUserSeqsBy(
		final @RequestBody List<String> userEmails
	) {
		return Response.successOk(userFacade.getUserSeqsBy(userEmails));
	}

}
