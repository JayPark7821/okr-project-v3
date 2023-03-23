package kr.service.okr.acceptance.project.keyresult;

import java.util.regex.Pattern;

import org.assertj.core.api.AssertionsForClassTypes;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.service.okr.common.exception.ErrorCode;

public class KeyResultAccptanceTestAssertions {

	static void 핵심결과_추가_요청_응답_검증(ExtractableResponse<Response> 응답) {
		AssertionsForClassTypes.assertThat(응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		AssertionsForClassTypes.assertThat(응답.body().asString()).containsPattern(
			Pattern.compile("keyResult-[a-zA-Z0-9]{10}"));
	}

	static void 핵심결과_추가_요청_응답_검증_실패_핵심결과_갯수_초과(ExtractableResponse<Response> 응답) {
		AssertionsForClassTypes.assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		AssertionsForClassTypes.assertThat(응답.body().asString())
			.isEqualTo(ErrorCode.KEYRESULT_LIMIT_EXCEED.getMessage());
	}

	static void 핵심결과_추가_요청_응답_검증_실패_팀원(ExtractableResponse<Response> 응답) {
		AssertionsForClassTypes.assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		AssertionsForClassTypes.assertThat(응답.body().asString()).isEqualTo(ErrorCode.USER_IS_NOT_LEADER.getMessage());
	}
}
