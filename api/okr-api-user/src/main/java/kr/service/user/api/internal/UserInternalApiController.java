package kr.service.user.api.internal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "okr-boot-user", url = "${okr.user-auth-service.url}")
public interface UserInternalApiController {

	@GetMapping("/api/v1/user/auth")
	ResponseEntity<UserInfoResponse> getUserInfoBy(
		final @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt
	);
}
