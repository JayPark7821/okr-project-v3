package kr.service.okr.acceptance.project.initiative;

import static kr.service.okr.acceptance.project.initiative.InitiativeAcceptanceTestAssertions.*;
import static kr.service.okr.acceptance.project.initiative.InitiativeAcceptanceTestSteps.*;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.service.okrcommon.common.utils.JwtTokenUtils;
import kr.service.okr.interfaces.project.request.ProjectInitiativeSaveRequest;
import kr.service.okr.util.SpringBootTestReady;
import kr.service.okr.util.TestHelpUtils;

@DisplayName("Initiative(행동전략) 도메인 인수 테스트")
public class InitiativeAcceptanceTest extends SpringBootTestReady {

	@PersistenceContext
	EntityManager em;
	String 사용자_토큰;
	String 행동전략_조회_유저_토큰;

	@BeforeEach
	void beforeEach() {
		super.setUp();
		dataLoader.loadData(List.of("/insert-project-data.sql"));
		사용자_토큰 = JwtTokenUtils.generateToken("projectMasterTest@naver.com", key, 엑세스_토큰_유효기간_임계값);
		행동전략_조회_유저_토큰 = JwtTokenUtils.generateToken("initiativeRetrieveTest@naver.com", key, 엑세스_토큰_유효기간_임계값);
	}

	@Test
	@DisplayName("행동전략 추가시 기대하는 응답(initiativeToken)을 반환한다.")
	void add_initiative() throws Exception {
		//given
		var 행동전략_생성_데이터 = 행동전략_저장_요청_데이터_생성("행동전략1", "key_wV6MX15WQ3DTzQMs", "행동전략 상세");

		//when
		var 응답 = 횅동전략_추가_요청(행동전략_생성_데이터, 사용자_토큰);

		//then
		행동전략_추가_요청_응답_검증(응답);
	}

	@Test
	@DisplayName("행동전략 추가시 진척도가 변경된다 (동시성 테스트)")
	void project_progress_will_change_when_initiative_added_concurrency_test() throws Exception {
		//given
		var 행동전략_생성_데이터 = 행동전략_저장_요청_데이터_생성("행동전략1", "key_wV6MX15WQ3DTzQMs", "행동전략 상세");

		//when
		행동전략_추가_요청_동시성_테스트(행동전략_생성_데이터, 사용자_토큰);

		//then
		행동전략_추가_요청_동시성_테스트_검증(em);
	}

	@Test
	@DisplayName("행동전략 완료 처리시 기대하는 응답을 반환한다.")
	void make_initiative_done() throws Exception {
		//given
		var 완료_처리할_행동전략_토큰 = "ini_iefefawef3f23fdb3AH8";

		//when
		var 응답 = 행동전략_완료_요청(완료_처리할_행동전략_토큰, 행동전략_조회_유저_토큰);

		//then
		행동전략_완료_요청_응답_검증(응답, 완료_처리할_행동전략_토큰);
	}

	@Test
	@DisplayName("핵심결과에 등록된 행동전략 리스트 조회시 기대하는 응답을 리턴한다")
	void get_initiative_list_by_keyReusltToken() throws Exception {
		//given
		var 행동전략_조회용_핵심결과_토큰 = "key_wV6faaaaaaDTzQ22";

		//when
		var 응답 = 행동전략_리스트_조회_요청(행동전략_조회용_핵심결과_토큰, 행동전략_조회_유저_토큰);

		//then
		행동전략_리스트_조회_응답_검증(응답);

	}

	@Test
	@DisplayName("행동전략 토큰으로 행동전략 상세정보를 조회하면 기대하는 응답을 리턴한다")
	void get_initiative_detail_by_initiativeToken() throws Exception {
		//given
		var 행동전략_상세_조회용_토큰 = "ini_iefefawef3fdab3AH8";

		//when
		var 응답 = 행동전략_상세_조회_요청(행동전략_상세_조회용_토큰, 행동전략_조회_유저_토큰);

		//then
		행동전략_상세_조회_응답_검증(응답);
	}

	@Test
	@DisplayName("날짜로 행동전략을 조회하면 미완료 행동전략을 리스트를 리턴한다")
	void get_initiative_by_date() throws Exception {
		//given
		var 행동전략_조회_날짜 = "20231201";

		//when
		var 응답 = 날짜로_행동전략_조회_요청(행동전략_조회_날짜, 행동전략_조회_유저_토큰);

		//then
		행동전략_날짜_조회_응답_검증(응답);
	}

	@Test
	@DisplayName("잘못된 날짜 포멧으로 행동전략 조회를 요청하면 기대하는 응답을 리턴한다")
	void get_initiative_by_wrong_date_format() throws Exception {
		//given
		var 행동전략_조회_날짜_잘못된_포멧 = "2023-12-01";

		//when
		var 응답 = 날짜로_행동전략_조회_요청(행동전략_조회_날짜_잘못된_포멧, 행동전략_조회_유저_토큰);

		//then
		행동전략_날짜_조회_잘못된_포멧_응답_검증(응답);
	}

	@Test
	@DisplayName("년월로 행동전략이 진행중인 일자 리스트를 조회하면 기대하는 응답을 리턴한다")
	void get_initiative_active_list_by_year_month() throws Exception {
		//given
		var 행동전략_조회_년월 = "2023-12";

		//when
		var 응답 = 년월로_행동전략_진행중인_일자_리스트_조회_요청(행동전략_조회_년월, 행동전략_조회_유저_토큰);

		//then
		행동전략_년월_조회_응답_검증(응답);
	}

	@Test
	@DisplayName("잘못된 포멧의 년월로 행동전략이 진행중인 일자 리스트를 조회하면 기대하는 응답을 리턴한다")
	void get_initiative_active_list_by_year_month_wrong_format() throws Exception {
		//given
		var 행동전략_조회_년월_잘못된_포멧 = "202312";

		//when
		var 응답 = 년월로_행동전략_진행중인_일자_리스트_조회_요청(행동전략_조회_년월_잘못된_포멧, 사용자_토큰);

		//then
		행동전략_년월_조회_잘못된_포멧_응답_검증(응답);
	}

	@Test
	@DisplayName("행동전략 수정을 요청하면 기대하는 응답을 리턴한다")
	void update_initiative() throws Exception {
		//given
		// var 행동전략_수정_요청_데이터 = 행동전략_수정_요청_데이터_생성();
		//
		// //when
		// var 응답 = 행동전략_수정_요청(행동전략_수정_요청_데이터, 사용자_토큰);
		//
		// //then
		// 행동전략_수정_응답_검증(응답);
	}

	private ProjectInitiativeSaveRequest 행동전략_저장_요청_데이터_생성(String 행동전략명, String 핵심결과_토큰, String 행동전략_상세_내용) {
		return new ProjectInitiativeSaveRequest(
			핵심결과_토큰,
			행동전략명,
			TestHelpUtils.getDateString(10, "yyyy-MM-dd"),
			TestHelpUtils.getDateString(-10, "yyyy-MM-dd"),
			행동전략_상세_내용
		);
	}
}
