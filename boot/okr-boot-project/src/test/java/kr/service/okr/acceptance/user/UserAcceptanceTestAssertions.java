package kr.service.okr.acceptance.user;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.springframework.http.HttpStatus;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class UserAcceptanceTestAssertions {

	static void 로그인_응답_검증_게스트(ExtractableResponse<Response> 응답) {

		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());

		JsonPath response = 응답.body().jsonPath();
		assertThat(response.getString("guestUserId")).isNotNull();
		assertThat(response.getString("email")).isNotNull();
		assertThat(response.getString("name")).isNotNull();
		assertThat(response.getString("providerType")).isNotNull();
		assertThat(response.getString("accessToken")).isNull();
		assertThat(response.getString("refreshToken")).isNull();
		assertThat(response.getString("jobFieldDetail")).isNull();
	}

	static void 로그인_응답_검증_회원(ExtractableResponse<Response> 응답) {

		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());

		JsonPath response = 응답.body().jsonPath();
		assertThat(response.getString("guestUserId")).isNull();
		assertThat(response.getString("email")).isNotNull();
		assertThat(response.getString("name")).isNotNull();
		assertThat(response.getString("providerType")).isNotNull();
		assertThat(response.getString("accessToken")).isNotNull();
		assertThat(response.getString("refreshToken")).isNotNull();
		assertThat(response.getString("jobFieldDetail")).isNotNull();
	}

}
