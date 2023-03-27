package kr.service.okr.acceptance.project;

import static kr.service.okr.acceptance.project.ProjectAcceptanceTestAssertions.*;
import static kr.service.okr.acceptance.project.ProjectAcceptanceTestSteps.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.service.okr.common.utils.JwtTokenUtils;
import kr.service.okr.interfaces.project.request.ProjectSaveRequest;
import kr.service.okr.util.SpringBootTestReady;

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
		var 프로젝트_생성_요청_데이터 = 프로젝트_생성_요청_데이터_생성("프로젝트 목표", List.of());

		//when
		var 응답 = 프로젝트_생성_요청(프로젝트_생성_요청_데이터, 사용자_토큰);

		//then
		프로젝트_생성_요청_응답_검증(응답);
	}

	@Test
	@DisplayName("프로젝트를 생성시 시작&종료 일자 포멧을 잘못 입력하면 기대하는 응답(exception)을 반환한다.")
	void create_project_with_wrong_date_format() throws Exception {
		//given
		var 프로젝트_생성_요청_데이터 = 날짜_포멧이_잘못된_프로젝트_생성_요청_데이터_생성("프로젝트 목표", List.of());

		//when
		var 응답 = 프로젝트_생성_요청(프로젝트_생성_요청_데이터, 사용자_토큰);

		//then
		프로젝트_생성_요청_실패_응답_검증_날짜_포멧_오류(응답);
	}

	@Test
	@DisplayName("없는 팀원을 추가하여 프로젝트를 생성하면 기대하는 응답(exception)을 반환한다.")
	void create_project_with_not_exsist_user() throws Exception {
		//given
		var 프로젝트_생성_요청_데이터 = 프로젝트_생성_요청_데이터_생성("프로젝트 목표", List.of("not-exist-user"));

		//when
		var 응답 = 프로젝트_생성_요청(프로젝트_생성_요청_데이터, 사용자_토큰);

		//then
		프로젝트_생성_요청_실패_응답_검증_가입하지_않은_유저(응답);
	}

	@Test
	@DisplayName("회원 탈퇴전 참여중인 프로젝트 리스트를 조회하면 기대하는 응답(ParticipateProjectResponse)를 반환한다.")
	void get_participate_project_list() throws Exception {
		//when
		var 응답 = 회원탈퇴_요청전_참여중인_프로젝트_조회(사용자_토큰);

		//then
		참여중인_프로젝트_리스트_응답_검증(응답);
	}

	@Test
	@DisplayName("팀원을 추가하여 프로젝트를 생성하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project_with_team_members() throws Exception {
		//given
		var 프로젝트_생성_요청_데이터 = 프로젝트_생성_요청_데이터_생성("프로젝트 목표", List.of("user7@naver.com"));

		//when
		var 응답 = 프로젝트_생성_요청(프로젝트_생성_요청_데이터, 사용자_토큰);

		//then
		프로젝트_생성_요청_응답_검증(응답);
	}

	@Test
	@DisplayName("projectToken으로 조회하면 기대하는 응답(ProjectResponse)을 반환한다.")
	void retrieve_project_with_project_token() throws Exception {
		//given
		var 프로젝트_토큰 = "mst_Kiwqnp1Nq6lbTNn0";

		//when
		var 응답 = 프로젝트_조회_요청(프로젝트_토큰, 사용자_토큰);

		//then
		프로젝트_조회_응답_검증(응답);
	}

	@Test
	@DisplayName("메인 페이지 프로젝트 조회시 조건에 따라 기대하는 응답을 반환한다 ( 최근 생성순, 종료된 프로젝트 포함, 팀 프로젝트 )")
	void retrieve_projects_for_main_page() throws Exception {
		//given
		var 정렬순서 = "RECENTLY_CREATE";
		var 종료된_프로젝트_포함여부 = "N";
		var 팀_타입 = "TEAM";
		var 프로젝트_조회_사용자_토큰 = JwtTokenUtils.generateToken("projectMasterRetrieveTest@naver.com", key, 엑세스_토큰_유효기간_임계값);

		//when
		var 응답 = 메인_페이지_프로젝트_조회_요청(정렬순서, 종료된_프로젝트_포함여부, 팀_타입, 프로젝트_조회_사용자_토큰);

		//then
		메인_페이지_프로젝트_조회_응답_검증(응답);
	}

	@Test
	@DisplayName("프로젝트 사이드 메뉴 조회시 기대하는 응답을 리턴한다")
	void retrieve_project_side_menu() throws Exception {
		//given
		var 프로젝트_토큰 = "mst_K4g4tfdaergg6421";
		var 프로젝트_조회_사용자_토큰 = JwtTokenUtils.generateToken("projectMasterRetrieveTest@naver.com", key, 엑세스_토큰_유효기간_임계값);

		//when
		var 응답 = 프로젝트_사이드_메뉴_조회_요청(프로젝트_토큰, 프로젝트_조회_사용자_토큰);

		//then
		프로젝트_사이드_메뉴_조회_응답_검증(응답);

	}

	private ProjectSaveRequest 프로젝트_생성_요청_데이터_생성(String 목표, List<String> 팀원) {

		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		return new ProjectSaveRequest(목표, projectSdt, projectEdt, 팀원);
	}

	private ProjectSaveRequest 날짜_포멧이_잘못된_프로젝트_생성_요청_데이터_생성(String 목표, List<String> 팀원) {

		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		return new ProjectSaveRequest(목표, projectSdt, projectEdt, 팀원);
	}

}
