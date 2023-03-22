package kr.service.okr.acceptance.project;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.List;
import java.util.regex.Pattern;

import org.assertj.core.api.AssertionsForClassTypes;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.service.okr.common.exception.ErrorCode;
import kr.service.okr.domain.project.aggregate.team.ProjectRoleType;
import kr.service.okr.interfaces.project.response.ParticipateProjectResponse;

public class ProjectAcceptanceTestAssertions {

	static void 프로젝트_생성_요청_응답_검증(ExtractableResponse<Response> 응답) {

		AssertionsForClassTypes.assertThat(응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		AssertionsForClassTypes.assertThat(응답.body().asString()).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));
	}

	static void 프로젝트_생성_요청_실패_응답_검증_날짜_포멧_오류(ExtractableResponse<Response> 응답) {
		AssertionsForClassTypes.assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		AssertionsForClassTypes.assertThat(응답.body().asString()).isEqualTo("8자리의 yyyy-MM-dd 형식이어야 합니다.");
	}

	static void 프로젝트_생성_요청_실패_응답_검증_가입하지_않은_유저(ExtractableResponse<Response> 응답) {
		AssertionsForClassTypes.assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		AssertionsForClassTypes.assertThat(응답.body().asString()).isEqualTo(ErrorCode.INVALID_USER_EMAIL.getMessage());
	}

	static void 참여중인_프로젝트_리스트_응답_검증(ExtractableResponse<Response> 응답) {
		AssertionsForClassTypes.assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
		final List<ParticipateProjectResponse> response = 응답.body()
			.jsonPath()
			.getList("", ParticipateProjectResponse.class);
		assertThat(response.size()).isEqualTo(3);
		assertThat(
			response.stream()
				.filter(t -> t.roleType().equals(ProjectRoleType.LEADER))
				.toList()
				.size()
		).isEqualTo(2);
	}

}
