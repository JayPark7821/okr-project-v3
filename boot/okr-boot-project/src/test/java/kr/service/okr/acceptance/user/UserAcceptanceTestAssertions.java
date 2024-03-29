package kr.service.okr.acceptance.user;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.Arrays;

import org.springframework.http.HttpStatus;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.service.okr.exception.ErrorCode;
import kr.service.okr.user.api.JobResponse;
import kr.service.okr.user.api.TokenResponse;
import kr.service.okr.user.enums.JobCategory;

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

	static void 로그인_실패_검증_구글_가입(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(응답.body().asString()).contains(ErrorCode.MISS_MATCH_PROVIDER.getMessage().formatted("GOOGLE"));
	}

	static void 회원가입_요청_성공_검증(ExtractableResponse<Response> 응답) {

		assertThat(응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		JsonPath response = 응답.body().jsonPath();
		assertThat(response.getString("guestUserId")).isNull();
		assertThat(response.getString("email")).isNotNull();
		assertThat(response.getString("name")).isNotNull();
		assertThat(response.getString("providerType")).isNotNull();
		assertThat(response.getString("accessToken")).isNotNull();
		assertThat(response.getString("refreshToken")).isNotNull();
		assertThat(response.getString("jobFieldDetail")).isNotNull();
	}

	static void 게스트_정보_없을_때_회원가입_요청_실패_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(응답.body().asString()).contains(ErrorCode.INVALID_JOIN_INFO.getMessage());
	}

	static void 이미_가입한_유저_회원가입_요청_실패_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(응답.body().asString()).contains(ErrorCode.ALREADY_JOINED_USER.getMessage());
	}

	static void 직업_목록_응답_검증(ExtractableResponse<Response> 직업_목록, JobCategory 카테고리) {
		assertThat(직업_목록.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(직업_목록.body().jsonPath().getList("", JobResponse.class))
			.isEqualTo(카테고리.getDetailList().stream().map(j -> new JobResponse(j.name(), j.getTitle())).toList());
	}

	static void 직업_카테고리_목록_응답_검증(ExtractableResponse<Response> 직업_카테고리_목록) {
		assertThat(직업_카테고리_목록.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(직업_카테고리_목록.body().jsonPath().getList("", JobResponse.class))
			.isEqualTo(
				Arrays.stream(JobCategory.values()).map(j -> new JobResponse(j.name(), j.getTitle())).toList());
	}

	static void 토큰_응답_검증(ExtractableResponse<Response> 응답, String refreshToken) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
		TokenResponse response = 응답.body().jsonPath().getObject("", TokenResponse.class);
		assertThat(response.refreshToken()).isEqualTo(refreshToken);
		assertThat(response.accessToken()).isNotNull();

	}

	static void 토큰_응답_검증_새로운_refreshToken(ExtractableResponse<Response> 응답, String refreshToken) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
		TokenResponse response = 응답.body().jsonPath().getObject("", TokenResponse.class);
		assertThat(response.refreshToken()).isNotEqualTo(refreshToken);
		assertThat(response.accessToken()).isNotNull();

	}
}
