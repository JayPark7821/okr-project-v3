package kr.service.okr.acceptance.project;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.service.okr.project.api.ProjectInfoResponse;
import kr.service.okr.project.domain.enums.ProjectRoleType;

public class ProjectAcceptanceTestAssertions {

	static void 프로젝트_생성_요청_응답_검증(ExtractableResponse<Response> 응답) {

		assertThat(응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(응답.body().asString()).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));
	}

	static void 프로젝트_조회_요청_응답_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
		final ProjectInfoResponse response = 응답.body().jsonPath().getObject("", ProjectInfoResponse.class);
		assertThat(response.projectToken()).isEqualTo("mst_Kiwqnp1Nq6lbTNn0");
		assertThat(response.objective()).isEqualTo("팀 맴버 테스트용 프로젝트");
		assertThat(response.startDate()).isEqualTo("2022-12-07");
		assertThat(response.endDate()).isEqualTo("3999-12-14");
		assertThat(response.projectType()).isEqualTo("TEAM");
		assertThat(response.teamMembersCount()).isEqualTo(3);
		assertThat(response.roleType()).isEqualTo(ProjectRoleType.LEADER.name());
		assertThat(response.keyResults().size()).isEqualTo(3);

	}

}
