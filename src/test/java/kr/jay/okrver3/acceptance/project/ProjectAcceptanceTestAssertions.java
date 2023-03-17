package kr.jay.okrver3.acceptance.project;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.jay.okrver3.common.exception.ErrorCode;

public class ProjectAcceptanceTestAssertions {

	static void 프로젝트_생성_요청_응답_검증(ExtractableResponse<Response> 응답) {

		assertThat(응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(응답.body().asString()).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));
	}

	static void 프로젝트_생성_요청_실패_응답_검증_날짜_포멧_오류(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(응답.body().asString()).isEqualTo("8자리의 yyyy-MM-dd 형식이어야 합니다.");
	}

	static void 프로젝트_생성_요청_실패_응답_검증_가입하지_않은_유저(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(응답.body().asString()).isEqualTo(ErrorCode.INVALID_USER_EMAIL.getMessage());
	}

	static void 핵심결과_추가_요청_응답_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(응답.body().asString()).containsPattern(
			Pattern.compile("keyResult-[a-zA-Z0-9]{10}"));
	}

	static void 핵심결과_추가_요청_응답_검증_실패_핵심결과_갯수_초과(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(응답.body().asString()).isEqualTo(ErrorCode.KEYRESULT_LIMIT_EXCEED.getMessage());
	}

	static void 핵심결과_추가_요청_응답_검증_실패_팀원(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(응답.body().asString()).isEqualTo(ErrorCode.USER_IS_NOT_LEADER.getMessage());
	}

	static void 행동전략_추가_요청_응답_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(응답.body().asString()).containsPattern(
			Pattern.compile("initiative-[a-zA-Z0-9]{9}"));
	}

}
