package kr.service.okr.acceptance.project.initiative;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.regex.Pattern;

import jakarta.persistence.EntityManager;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.service.okrcommon.common.exception.ErrorCode;
import kr.service.okr.domain.project.Project;
import kr.service.okr.domain.project.aggregate.initiative.Initiative;
import kr.service.okr.domain.project.info.TeamMemberUserInfo;
import kr.service.okr.interfaces.project.response.InitiativeDetailResponse;
import kr.service.okr.interfaces.project.response.InitiativeForCalendarResponse;
import kr.service.okr.interfaces.project.response.ProjectInitiativeResponse;

public class InitiativeAcceptanceTestAssertions {

	static void 행동전략_추가_요청_응답_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(응답.body().asString()).containsPattern(
			Pattern.compile("initiative-[a-zA-Z0-9]{9}"));
	}

	static void 행동전략_추가_요청_동시성_테스트_검증(EntityManager em) throws Exception {
		Thread.sleep(100L);

		Project project = em.createQuery(
				"select p from Project p where p.id = :id", Project.class)
			.setParameter("id", 99998L)
			.getSingleResult();

		List<Initiative> initiatives = em.createQuery(
				"select i from Initiative i where i.keyResult.id = :id", Initiative.class)
			.setParameter("id", 99999L)
			.getResultList();

		assertThat(initiatives.size()).isEqualTo(100);
		assertThat(project.getProgress()).isEqualTo(1.0);
	}

	static void 행동전략_완료_요청_응답_검증(ExtractableResponse<Response> 응답, String 완료_처리한_행동전략_토큰) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(응답.body().asString()).isEqualTo(완료_처리한_행동전략_토큰);
	}

	static void 행동전략_리스트_조회_응답_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
		var response = 응답.body()
			.jsonPath()
			.getList("content", ProjectInitiativeResponse.class);
		assertThat(response.size()).isEqualTo(6);
	}

	static void 행동전략_상세_조회_응답_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
		var response = 응답.body().jsonPath().getObject("", InitiativeDetailResponse.class);
		assertThat(response.done()).isFalse();
		// assertThat(response.initiativeToken()).isEqualTo(initiativeToken);
		assertThat(response.initiativeName()).isEqualTo("ini name");
		assertThat(response.initiativeDetail()).isEqualTo("initiative detail1");
		assertThat(response.myInitiative()).isTrue();
		TeamMemberUserInfo responseUser = response.user();
		assertThat(responseUser.userEmail()).isEqualTo("initiativeRetrieveTest@naver.com");
	}

	static void 행동전략_날짜_조회_응답_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
		var response = 응답.body().jsonPath().getList("", InitiativeForCalendarResponse.class);
		assertThat(response.size()).isEqualTo(3);
	}

	static void 행동전략_날짜_조회_잘못된_포멧_응답_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(응답.body().asString()).isEqualTo(ErrorCode.INVALID_SEARCH_DATE_FORM.getMessage());
	}

	static void 행동전략_년월_조회_응답_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
		var response = 응답.body().jsonPath().getList("", String.class);
		assertThat(response.size()).isEqualTo(14);
		assertThat(response.get(0)).isEqualTo("2023-12-01");
		assertThat(response.get(response.size() - 1)).isEqualTo("2023-12-14");
	}

	static void 행동전략_년월_조회_잘못된_포멧_응답_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(응답.body().asString()).isEqualTo(ErrorCode.INVALID_YEARMONTH_FORMAT.getMessage());

	}

}
