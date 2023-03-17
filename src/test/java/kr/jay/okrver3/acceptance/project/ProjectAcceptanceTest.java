package kr.jay.okrver3.acceptance.project;

import static kr.jay.okrver3.acceptance.project.ProjectAcceptanceTestAssertions.*;
import static kr.jay.okrver3.acceptance.project.ProjectAcceptanceTestSteps.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.jay.okrver3.common.utils.JwtTokenUtils;
import kr.jay.okrver3.interfaces.project.request.ProjectSaveRequest;
import kr.jay.okrver3.util.SpringBootTestReady;

@DisplayName("Project 도메인 인수 테스트")
public class ProjectAcceptanceTest extends SpringBootTestReady {

	String 사용자_토큰;

	@BeforeEach
	void beforeEach() {
		super.setUp();
		dataLoader.loadData(List.of("/insert-project-date.sql"));
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

}
