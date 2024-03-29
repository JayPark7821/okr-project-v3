package kr.service.oauth.platform.apple;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "appleClient", url = "https://appleid.apple.com/auth")
public interface AppleClient {
	@GetMapping(value = "/keys")
	ApplePublicKeyResponse getAppleAuthPublicKey();

}