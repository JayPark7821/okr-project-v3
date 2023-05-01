package kr.service.okr.acceptance.project.initiative;

import static org.assertj.core.api.Assertions.*;

import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class InitiativeAcceptanceTestAssertions {
	static void 행동전략_추가_요청_응답_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(응답.body().asString()).containsPattern(
			Pattern.compile("initiative-[a-zA-Z0-9]{9}"));
	}

}
