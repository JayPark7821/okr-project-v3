package kr.jay.okrver3.interfaces.project;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.transaction.annotation.Transactional;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import kr.jay.okrver3.common.utils.JwtTokenUtils;

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProjectApiControllerAcceptanceTest {

	@Value("${app.auth.tokenSecret}")
	private String key;

	@Value("${app.auth.tokenExpiry}")
	private Long accessExpiredTimeMs;

	@Autowired
	DataSource dataSource;
	private static final String baseUrl = "/api/v1/project/";
	@LocalServerPort
	private int port;

	private String authToken;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
		authToken = JwtTokenUtils.generateToken("apple@apple.com", key, accessExpiredTimeMs);
		try (Connection conn = dataSource.getConnection()) {
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/insert-user.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/insert-project.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/insert-team.sql"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("프로젝트를 생성하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project() throws Exception {
		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		final String response = RestAssured.

			given()
			.header("Authorization", "Bearer " + authToken)
			.contentType(ContentType.JSON)
			.body(new ProjectMasterSaveDto("projectName", projectSdt, projectEdt, "projectObjective",
				List.of("keyResult1", "keyResult2"))).

			when()
			.post(baseUrl).

			then()
			.statusCode(HttpStatus.CREATED.value())
			.extract().body().asString();

		assertThat(response).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));

	}

	@Test
	@DisplayName("projectToken으로 조회하면 기대하는 응답(ProjectResponse)을 반환한다.")
	void retrieve_project_with_project_token() throws Exception {

		final JsonPath response = RestAssured.

			given()
			.header("Authorization", "Bearer " + authToken)
			.contentType(ContentType.JSON).

			when()
			.get(baseUrl + "project-fgFHxGWeIUQt").

			then()
			.statusCode(HttpStatus.CREATED.value())
			.extract().jsonPath();

		assertThat(response.getString("projectToken")).isEqualTo("project-fgFHxGWeIUQt");
		assertThat(response.getString("name")).isEqualTo("projectName");
		assertThat(response.getString("objective")).isEqualTo("projectObjective");
		assertThat(response.getString("startDate")).isEqualTo("2020-12-01");
		assertThat(response.getString("endDate")).isEqualTo("2020-12-12");
		assertThat(response.getString("projectType")).isEqualTo("SINGLE");

	}

	@Test
	@DisplayName("팀원 추가를 시도하면 기대하는 응답(추가된 email주소)을 반환한다.")
	void invite_team_member() throws Exception {
		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + authToken)
			.body(new TeamMemberInviteRequestDto("project-fgFHxGWeIUQt", "fakeAppleEmail")).

			when()
			.post(baseUrl + "team/invite").

			then()
			.statusCode(HttpStatus.CREATED.value())
			.extract().body().asString();

		assertThat(response).isEqualTo("fakeAppleEmail");
	}

}



