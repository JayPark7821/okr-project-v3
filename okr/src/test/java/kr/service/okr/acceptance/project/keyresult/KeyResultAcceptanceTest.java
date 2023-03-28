package kr.service.okr.acceptance.project.keyresult;

import static kr.service.okr.acceptance.project.keyresult.KeyResultAcceptanceTestSteps.*;
import static kr.service.okr.acceptance.project.keyresult.KeyResultAccptanceTestAssertions.*;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.service.okrcommon.common.utils.JwtTokenUtils;
import kr.service.okr.util.SpringBootTestReady;

@DisplayName("KeyResult(핵심결과) 도메인 인수 테스트")
public class KeyResultAcceptanceTest extends SpringBootTestReady {

	@PersistenceContext
	EntityManager em;
	String 사용자_토큰;

	@BeforeEach
	void beforeEach() {
		super.setUp();
		dataLoader.loadData(List.of("/insert-project-data.sql"));
		사용자_토큰 = JwtTokenUtils.generateToken("projectMasterTest@naver.com", key, 엑세스_토큰_유효기간_임계값);
	}

	@Test
	@DisplayName("핵심결과 추가 요청시 기대하는 응답(핵심결과 토큰)을 반환한다.")
	void add_keyResult_to_project() throws Exception {
		//given
		var 프로젝트_토큰 = "mst_as3fg34tgg6421";

		//when
		var 응답 = 핵심결과_추가_요청(프로젝트_토큰, "핵심결과1", 사용자_토큰);

		//then
		핵심결과_추가_요청_응답_검증(응답);
	}

	@Test
	@DisplayName("등록 가능한 모든 핵심결과가 등록되었을 때 핵심결과 추가 요청시 기대하는 응답(exception)을 반환한다.")
	void add_keyResult_to_fully_added_keyResult_project() throws Exception {
		//given
		var 프로젝트_토큰 = "mst_Kiwqnp1Nq6lbTNn0";

		//when
		var 응답 = 핵심결과_추가_요청(프로젝트_토큰, "핵심결과1", 사용자_토큰);

		//then
		핵심결과_추가_요청_응답_검증_실패_핵심결과_갯수_초과(응답);
	}

	@Test
	@DisplayName("프로젝트 리더가 아닌 팀원이 핵심결과 추가 요청시 기대하는 응답(exception)을 반환한다.")
	void member_add_keyResult_to_project() throws Exception {
		//given
		var 프로젝트_토큰 = "mst_qq2f4gbfffffe421";

		//when
		var 응답 = 핵심결과_추가_요청(프로젝트_토큰, "핵심결과1", 사용자_토큰);

		//then
		핵심결과_추가_요청_응답_검증_실패_팀원(응답);
	}

}
