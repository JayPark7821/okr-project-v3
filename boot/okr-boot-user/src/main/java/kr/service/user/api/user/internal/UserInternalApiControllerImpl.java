package kr.service.user.api.user.internal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.service.user.Response;
import kr.service.user.api.internal.UserInfo;
import kr.service.user.api.internal.UserInternalApiController;

@RestController
@RequestMapping("/api/v1/user")
public class UserInternalApiControllerImpl implements UserInternalApiController {

	@Override
	@GetMapping("/auth")
	public ResponseEntity<UserInfo> getUserSeqByEmail(final String jwt) {
		return Response.successOk(new UserInfo(1L, "1212", "awefwaef", "awefef"));
	}
}
