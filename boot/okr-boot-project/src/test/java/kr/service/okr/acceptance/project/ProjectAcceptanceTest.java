package kr.service.okr.acceptance.project;

import static kr.service.okr.acceptance.project.ProjectAcceptanceTestAssertions.*;
import static kr.service.okr.acceptance.project.ProjectAcceptanceTestSteps.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.tomakehurst.wiremock.WireMockServer;

import kr.service.jwt.JwtService;
import kr.service.okr.project.api.RegisterProjectRequestDto;
import kr.service.okr.utils.SpringBootTestReady;

@DisplayName("Project 도메인 인수 테스트")
public class ProjectAcceptanceTest extends SpringBootTestReady {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private WireMockServer wireMockServer;

	@BeforeEach
	void beforeEach() throws IOException {
		super.setUp();
		dataLoader.loadData(List.of("/project-test-data.sql"));
	}

	@Test
	@DisplayName("팀원 없이 프로젝트를 생성하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project() throws Exception {
		//given
		var 프로젝트_생성_요청_데이터 = 프로젝트_생성_요청_데이터_생성("프로젝트 목표", List.of());

		//when
		var 응답 = 프로젝트_생성_요청(프로젝트_생성_요청_데이터);

		//then
		프로젝트_생성_요청_응답_검증(응답);
	}

	@Test
	@DisplayName("프로젝트를 조회하면 기대하는 응답(ProjectInfoResponse)을 반환한다.")
	void query_project() throws Exception {
		//given
		var 프로젝트_토큰 = "mst_Kiwqnp1Nq6lbTNn0";
		var 로그인_인증_토큰 = jwtService.generateJwtToken("user@email.com", false);

		//when
		var 응답 = 프로젝트_조회_요청(프로젝트_토큰, 로그인_인증_토큰, wireMockServer);

		//then
		프로젝트_조회_요청_응답_검증(응답);
	}

	private RegisterProjectRequestDto 프로젝트_생성_요청_데이터_생성(String 목표, List<String> 팀원) {

		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		return new RegisterProjectRequestDto(목표, projectSdt, projectEdt, 팀원);
	}

}
