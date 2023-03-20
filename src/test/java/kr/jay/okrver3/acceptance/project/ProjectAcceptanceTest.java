package kr.jay.okrver3.acceptance.project;

import static kr.jay.okrver3.acceptance.project.ProjectAcceptanceTestAssertions.*;
import static kr.jay.okrver3.acceptance.project.ProjectAcceptanceTestSteps.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.jay.okrver3.common.utils.JwtTokenUtils;
import kr.jay.okrver3.interfaces.project.request.ProjectInitiativeSaveRequest;
import kr.jay.okrver3.interfaces.project.request.ProjectSaveRequest;
import kr.jay.okrver3.util.SpringBootTestReady;
import kr.jay.okrver3.util.TestHelpUtils;

@DisplayName("Project 도메인 인수 테스트")
public class ProjectAcceptanceTest extends SpringBootTestReady {

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
	@DisplayName("팀원 없이 프로젝트를 생성하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project() throws Exception {
		//given
		var 프로젝트_생성_요청_데이터 = 프로젝트_생성_요청_데이터_생성("프로젝트 목표", List.of("핵심결과 1", "핵심결과 2"), List.of());

		//when
		var 응답 = 프로젝트_생성_요청(프로젝트_생성_요청_데이터, 사용자_토큰);

		//then
		프로젝트_생성_요청_응답_검증(응답);
	}

	@Test
	@DisplayName("프로젝트를 생성시 시작&종료 일자 포멧을 잘못 입력하면 기대하는 응답(exception)을 반환한다.")
	void create_project_with_wrong_date_format() throws Exception {
		//given
		var 프로젝트_생성_요청_데이터 = 날짜_포멧이_잘못된_프로젝트_생성_요청_데이터_생성("프로젝트 목표", List.of("핵심결과 1", "핵심결과 2"), List.of());

		//when
		var 응답 = 프로젝트_생성_요청(프로젝트_생성_요청_데이터, 사용자_토큰);

		//then
		프로젝트_생성_요청_실패_응답_검증_날짜_포멧_오류(응답);
	}

	@Test
	@DisplayName("없는 팀원을 추가하여 프로젝트를 생성하면 기대하는 응답(exception)을 반환한다.")
	void create_project_with_not_exsist_user() throws Exception {
		//given
		var 프로젝트_생성_요청_데이터 = 프로젝트_생성_요청_데이터_생성("프로젝트 목표", List.of("핵심결과 1", "핵심결과 2"), List.of("not-exist-user"));

		//when
		var 응답 = 프로젝트_생성_요청(프로젝트_생성_요청_데이터, 사용자_토큰);

		//then
		프로젝트_생성_요청_실패_응답_검증_가입하지_않은_유저(응답);
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
	@DisplayName("회원 탈퇴전 참여중인 프로젝트 리스트를 조회하면 기대하는 응답(ParticipateProjectResponse)를 반환한다.")
	void get_participate_project_list() throws Exception {
		//when
		var 응답 = 회원탈퇴_요청전_참여중인_프로젝트_조회(사용자_토큰);

		//then
		참여중인_프로젝트_리스트_응답_검증(응답);
	}

	private ProjectSaveRequest 프로젝트_생성_요청_데이터_생성(String 목표, List<String> 핵심결과, List<String> 팀원) {

		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		return new ProjectSaveRequest(목표, projectSdt, projectEdt, 핵심결과, 팀원);
	}

	private ProjectSaveRequest 날짜_포멧이_잘못된_프로젝트_생성_요청_데이터_생성(String 목표, List<String> 핵심결과, List<String> 팀원) {

		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		return new ProjectSaveRequest(목표, projectSdt, projectEdt, 핵심결과, 팀원);
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
