package kr.jay.okrver3.acceptance.user;

import static kr.jay.okrver3.acceptance.user.UserAcceptanceTestData.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.Arrays;

import org.springframework.http.HttpStatus;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.domain.user.JobCategory;
import kr.jay.okrver3.interfaces.user.response.JobResponse;
import kr.jay.okrver3.interfaces.user.response.TokenResponse;
import kr.jay.okrver3.interfaces.user.response.UserInfoResponse;

public class UserAcceptanceTestAssertions {

	static void 직업_카테고리_응답_검증(ExtractableResponse<Response> 직업_목록) {
		assertThat(직업_목록.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(직업_목록.body().asString()).isEqualTo(JobCategory.BACK_END.getCode());
	}

	static void 직업_목록_응답_검증(ExtractableResponse<Response> 직업_목록, JobCategory 카테고리) {
		assertThat(직업_목록.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(직업_목록.body().jsonPath().getList("", JobResponse.class))
			.isEqualTo(카테고리.getDetailList().stream().map(j -> new JobResponse(j.getCode(), j.getTitle())).toList());
	}

	static void 직업_카테고리_목록_응답_검증(ExtractableResponse<Response> 직업_카테고리_목록) {
		assertThat(직업_카테고리_목록.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(직업_카테고리_목록.body().jsonPath().getList("", JobResponse.class))
			.isEqualTo(
				Arrays.stream(JobCategory.values()).map(j -> new JobResponse(j.getCode(), j.getTitle())).toList());
	}

	static void 이메일_검증_응답_검증(ExtractableResponse<Response> 응답, String 요청_이메일_주소) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(응답.body().asString()).isEqualTo(요청_이메일_주소);
	}

	static void 게스트_정보_없을_때_회원가입_요청_실패_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(응답.body().asString()).contains(ErrorCode.INVALID_JOIN_INFO.getMessage());
	}

	static void 이미_가입한_유저_회원가입_요청_실패_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(응답.body().asString()).contains(ErrorCode.ALREADY_JOINED_USER.getMessage());
	}

	static void 로그인_실패_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(응답.body().asString()).contains("소셜 provider 불일치,");
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

	static void 유저_정보_응답_검증(ExtractableResponse<Response> 응답) {

		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());

		UserInfoResponse response = 응답.body().jsonPath().getObject("", UserInfoResponse.class);
		assertThat(response.email()).isEqualTo(사용자1.getEmail());
		assertThat(response.name()).isEqualTo(사용자1.getUsername());
		assertThat(response.providerType()).isEqualTo(사용자1.getProviderType());
		assertThat(response.roleType()).isEqualTo(사용자1.getRoleType());
		assertThat(response.jobFieldDetail()).isEqualTo(사용자1.getJobField().getTitle());
		assertThat(response.profileImage()).isEqualTo(사용자1.getProfileImage());
	}

	static void 사용자_정보_수정_응답_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(응답.body().asString()).isEqualTo("SUCCESS");

	}

	static void 토큰_응답_검증(ExtractableResponse<Response> 응답, String accessToken, String refreshToken) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
		TokenResponse response = 응답.body().jsonPath().getObject("", TokenResponse.class);
		assertThat(response.refreshToken()).isEqualTo(refreshToken);
		assertThat(response.accessToken()).isNotEqualTo(accessToken);

	}

	static void 토큰_응답_검증_새로운_refreshToken(ExtractableResponse<Response> 응답, String accessToken, String refreshToken) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
		TokenResponse response = 응답.body().jsonPath().getObject("", TokenResponse.class);
		assertThat(response.refreshToken()).isNotEqualTo(refreshToken);
		assertThat(response.accessToken()).isNotEqualTo(accessToken);

	}

	static void 회원_탈퇴_요청_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(응답.body().asString()).isNotEqualTo("SUCCESS");

	}

}
