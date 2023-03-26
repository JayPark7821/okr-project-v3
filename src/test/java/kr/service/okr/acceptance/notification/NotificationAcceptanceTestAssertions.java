package kr.service.okr.acceptance.notification;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.List;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.service.okr.common.exception.ErrorCode;
import kr.service.okr.interfaces.notification.response.NotificationResponse;

public class NotificationAcceptanceTestAssertions {

	static void 알림_조회_요청_응답_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());

		final List<NotificationResponse> response = 응답.body()
			.jsonPath()
			.getList("content", NotificationResponse.class);
		assertThat(response.size()).isEqualTo(2);
	}

	static void 알림_확인_요청_응답_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	static void 알림_삭제_요청_응답_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	static void 알림_삭제_요청_응답_검증_실패(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(응답.body().asString()).isEqualTo(ErrorCode.INVALID_REQUEST.getMessage());
	}

}
