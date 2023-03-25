package kr.service.okr.acceptance.project.feedback;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.service.okr.interfaces.project.response.FeedbackDetailResponse;
import kr.service.okr.interfaces.project.response.IniFeedbackResponse;
import kr.service.okr.interfaces.project.response.ProjectInitiativeResponse;

public class FeedbackAcceptanceTestAssertions {

	static void 핵심결과_추가_요청_응답_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(응답.body().asString()).containsPattern(
			Pattern.compile("feedback-[a-zA-Z0-9]{11}")
		);
	}

	static void 피드백_조회_요청_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());

		final IniFeedbackResponse response =
			응답.body().jsonPath()
				.getObject("", IniFeedbackResponse.class);
		assertThat(response.myInitiative()).isFalse();
		assertThat(response.wroteFeedback()).isTrue();
		assertThat(response.feedback().size()).isEqualTo(2);
	}

	static void 피드백_남기지_않은_행동전략_카운트_조회_응답_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(응답.body().asString()).isEqualTo("1");
	}

	static void 받은_피드백_조회_응답_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
		final List<FeedbackDetailResponse> response = 응답.jsonPath()
			.getList("content", FeedbackDetailResponse.class);

		assertThat(response.size()).isEqualTo(2);

	}

	static void 피드백_상태_업데이트_응답_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(응답.body().asString()).isEqualTo("feedback_el6q34zazzSyWx9");
	}

	static void 피드백을_남겨야_하는_행동전략_응답_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(응답.body().jsonPath().getList("", ProjectInitiativeResponse.class).size())
			.isEqualTo(3);
	}

}
