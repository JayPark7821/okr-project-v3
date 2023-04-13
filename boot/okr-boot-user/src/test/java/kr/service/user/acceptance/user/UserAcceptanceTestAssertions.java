package kr.service.user.acceptance.user;

import static kr.service.user.utils.OAuth2UserInfoFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.regex.Pattern;

import org.assertj.core.api.AssertionsForClassTypes;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.service.user.api.LoginResponse;

public class UserAcceptanceTestAssertions {

	static void 로그인_요청_응답_검증_게스트(ExtractableResponse<Response> 응답) {

		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
		final LoginResponse response = 응답.body().jsonPath().getObject("", LoginResponse.class);
		assertThat(response.guestUserId()).containsPattern(
			Pattern.compile("guest-[a-zA-Z0-9]{14}")
		);
		assertThat(response.name()).isEqualTo(DiffAppleUserInfoFixture.NAME);
		assertThat(response.email()).isEqualTo(DiffAppleUserInfoFixture.EMAIL);
		assertThat(response.providerType()).isEqualTo(DiffAppleUserInfoFixture.PROVIDER_TYPE.name());
		assertThat(response.accessToken()).isNull();
		assertThat(response.refreshToken()).isNull();
	}
}
