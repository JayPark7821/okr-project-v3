package kr.jay.okrver3.interfaces.project;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
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
import kr.jay.okrver3.TestHelpUtils;
import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.utils.JwtTokenUtils;
import kr.jay.okrver3.domain.keyresult.KeyResult;
import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.interfaces.feedback.request.FeedbackSaveRequest;
import kr.jay.okrver3.interfaces.project.request.ProjectInitiativeSaveRequest;
import kr.jay.okrver3.interfaces.project.request.ProjectKeyResultSaveRequest;
import kr.jay.okrver3.interfaces.project.request.ProjectSaveRequest;
import kr.jay.okrver3.interfaces.project.request.TeamMemberInviteRequest;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProjectApiControllerAcceptanceTest {

	@Value("${app.auth.tokenSecret}")
	private String key;

	@Value("${app.auth.tokenExpiry}")
	private Long accessExpiredTimeMs;

	@PersistenceContext
	EntityManager em;

	@Autowired
	DataSource dataSource;
	private static final String baseUrl = "/api/v1";
	@LocalServerPort
	private int port;

	private String authToken;

	@BeforeAll
	void setUpAll() {
		try (Connection conn = dataSource.getConnection()) {
			authToken = JwtTokenUtils.generateToken("apple@apple.com", key, accessExpiredTimeMs);
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/insert-user.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/insert-project.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/insert-team.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/insert-keyresult.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/insert-initiative.sql"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@Test
	@DisplayName("팀원 없이 프로젝트를 생성하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project() throws Exception {
		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		final String response = RestAssured.

			given()
			.header("Authorization", "Bearer " + authToken)
			.contentType(ContentType.JSON)
			.body(new ProjectSaveRequest("projectObjective", projectSdt, projectEdt,
				List.of("keyResult1", "keyResult2"), null)).

			when()
			.post(baseUrl + "/project").

			then()
			.statusCode(HttpStatus.CREATED.value())
			.extract().body().asString();

		assertThat(response).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));

	}

	@Test
	@DisplayName("프로젝트를 생성시 시작&종료 일자 포멧을 잘못 입력하면 기대하는 응답(exception)을 반환한다.")
	void create_project_date_validation_fail() throws Exception {
		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		final String response = RestAssured.

			given()
			.header("Authorization", "Bearer " + authToken)
			.contentType(ContentType.JSON)
			.body(new ProjectSaveRequest("projectObjective", projectSdt, projectEdt,
				List.of("keyResult1", "keyResult2"), null)).

			when()
			.post(baseUrl + "/project").

			then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.extract().body().asString();

		assertThat(response).isEqualTo("8자리의 yyyy-MM-dd 형식이어야 합니다.");

	}

	@Test
	@DisplayName("팀원을 추가하여 프로젝트를 생성하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project_with_team_members() throws Exception {
		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		final String response = RestAssured.

			given()
			.header("Authorization", "Bearer " + authToken)
			.contentType(ContentType.JSON)
			.body(new ProjectSaveRequest("projectObjective", projectSdt, projectEdt,
				List.of("keyResult1", "keyResult2"), List.of("guest@email.com"))).

			when()
			.post(baseUrl + "/project").

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
			.get(baseUrl + "/project" + "/project-fgFHxGWeIUFa").

			then()
			.statusCode(HttpStatus.CREATED.value())
			.extract().jsonPath();

		assertThat(response.getString("projectToken")).isEqualTo("project-fgFHxGWeIUFa");
		assertThat(response.getString("objective")).isEqualTo("projectObjective2");
		assertThat(response.getString("startDate")).isEqualTo("2020-12-01");
		assertThat(response.getString("endDate")).isEqualTo("3999-12-12");
		assertThat(response.getString("projectType")).isEqualTo("SINGLE");

	}

	@Test
	@DisplayName("팀원 추가를 시도하면 기대하는 응답(추가된 email주소)을 반환한다.")
	void invite_team_member() throws Exception {
		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + authToken)
			.body(new TeamMemberInviteRequest("project-fgFHxGWeIUQt", "fakeAppleEmail")).

			when()
			.post(baseUrl + "/team/invite").

			then()
			.statusCode(HttpStatus.CREATED.value())
			.extract().body().asString();

		assertThat(response).isEqualTo("fakeAppleEmail");
	}

	@Test
	@DisplayName("팀원 추가를 위해 email을 입력하면 기대하는 응답(email)을 반환한다.")
	void validate_email_address() throws Exception {
		String memberEmail = "guest@email.com";
		final String response = RestAssured.

			given()
			.header("Authorization", "Bearer " + authToken).

			when()
			.get(baseUrl + "/team/invite" + "/project-fgFHxGWeIUQt" + "/" + memberEmail).

			then()
			.statusCode(HttpStatus.OK.value())
			.extract().body().asString();

		assertThat(response).isEqualTo(memberEmail);
	}

	@Test
	@DisplayName("로그인한 유저가 속하지 않은 프로젝트에 팀원 추가를 위해 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_with_not_participating_project_throw_exception() throws Exception {
		String memberEmail = "guest@email.com";

		final String response = RestAssured.

			given()
			.header("Authorization",
				"Bearer " + JwtTokenUtils.generateToken("fakeAppleEmail", key, accessExpiredTimeMs)).

			when()
			.get(baseUrl + "/team/invite" + "/project-fgFHxGWeIUQt" + "/" + memberEmail).

			then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.extract().body().asString();

		assertThat(response).isEqualTo(ErrorCode.INVALID_PROJECT_TOKEN.getMessage());
	}

	@Test
	@DisplayName("리더가 아닌 팀원이 팀원 추가를 위해 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void when_member_validate_email_address_will_throw_exception() throws Exception {
		String memberEmail = "guest@email.com";

		final String response = RestAssured.

			given()
			.header("Authorization",
				"Bearer " + JwtTokenUtils.generateToken("fakeGoogleIdEmail", key, accessExpiredTimeMs)).

			when()
			.get(baseUrl + "/team/invite" + "/project-fgFHxGWeIUQt" + "/" + memberEmail).

			then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.extract().body().asString();

		assertThat(response).isEqualTo(ErrorCode.USER_IS_NOT_LEADER.getMessage());
	}

	@Test
	@DisplayName("팀원 추가를 위해 잘못된 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_exception() throws Exception {
		String wrongEmailAdd = "wrongEmailAdd";
		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + authToken).

			when()
			.get(baseUrl + "/team/invite" + "/project-fgFHxGWeIUQt" + "/" + wrongEmailAdd).

			then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.extract().body().asString();

		assertThat(response).isEqualTo(ErrorCode.INVALID_USER_EMAIL.getMessage());
	}

	@Test
	@DisplayName("이미 팀에 초대된 팀원의 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_already_team_member() throws Exception {
		String teamMemberEmail = "fakeGoogleIdEmail";

		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + authToken).

			when()
			.get(baseUrl + "/team/invite" + "/project-fgFHxGWeIUQt" + "/" + teamMemberEmail).

			then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.extract().body().asString();

		assertThat(response).isEqualTo(ErrorCode.USER_ALREADY_PROJECT_MEMBER.getMessage());
	}

	@Test
	@DisplayName("로그인된 유저 자신의 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_login_user_email() throws Exception {
		String teamMemberEmail = "apple@apple.com";

		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + authToken).

			when()
			.get(baseUrl + "/team/invite" + "/project-fgFHxGWeIUQt" + "/" + teamMemberEmail).

			then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.extract().body().asString();

		assertThat(response).isEqualTo(ErrorCode.NOT_AVAIL_INVITE_MYSELF.getMessage());
	}

	@Test
	void 메인_페이지_프로젝트_조회시_조건에_따라_기대하는_응답을_리턴한다_최근생성순_종료된프로젝트_포함_팀프로젝트() throws Exception {
		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + authToken).

			when()
			.get(baseUrl + "/project" + "?" + "sortType=RECENTLY_CREATE" + "&" + "includeFinishedProjectYN=N" + "&"
				+ "projectType=TEAM").

			then()
			.statusCode(HttpStatus.OK.value())
			.extract().body().asString();

	}

	@Test
	void 프로젝트_사이드_메뉴_조회시_기대하는_응답을_리턴한다_progress_team_members() throws Exception {
		String projectToken = "project-fgFHxGWeIUQt";

		final JsonPath response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + authToken).

			when()
			.get(baseUrl + "/project" + "/" + projectToken + "/side").

			then()
			.statusCode(HttpStatus.OK.value())
			.extract().jsonPath();

		assertThat(response.getString("progress")).isEqualTo("100.0");
		assertThat(response.getList("teamMembers").size()).isEqualTo(2);
	}

	@Test
	void 프로젝트_핵심결과_추가시_기대하는_응답을_리턴한다_keyResultToken() throws Exception {
		String projectToken = "project-fgFHxGWeIUQt";
		String keyResultName = "keyResult";
		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + authToken)
			.body(new ProjectKeyResultSaveRequest(projectToken, keyResultName)).

			when()
			.post(baseUrl + "/keyresult").

			then()
			.statusCode(HttpStatus.CREATED.value())
			.extract().body().asString();

		assertThat(response).containsPattern(
			Pattern.compile("keyResult-[a-zA-Z0-9]{10}"));
	}

	@Test
	void 행동전략_추가시_기대하는_응답을_리턴한다_initiativeToken() throws Exception {

		ProjectInitiativeSaveRequest requestDto = new ProjectInitiativeSaveRequest(
			"key_wV6MX15WQ3DTzQMs",
			"행동전략",
			TestHelpUtils.getDateString(10, "yyyy-MM-dd"),
			TestHelpUtils.getDateString(-10, "yyyy-MM-dd"),
			"행동전략 상세내용"
		);

		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + authToken)
			.body(requestDto).

			when()
			.post(baseUrl + "/initiative").

			then()
			.statusCode(HttpStatus.CREATED.value())
			.extract().body().asString();

		assertThat(response).containsPattern(
			Pattern.compile("initiative-[a-zA-Z0-9]{9}"));

	}

	@Test
	void 행동전략_추가시_프로젝트_진척도가_변경된다_동시성테스트() throws Exception {

		ProjectInitiativeSaveRequest requestDto = new ProjectInitiativeSaveRequest(
			"key_wV6MX15WQ3DTzQMs",
			"행동전략",
			TestHelpUtils.getDateString(10, "yyyy-MM-dd"),
			TestHelpUtils.getDateString(-10, "yyyy-MM-dd"),
			"행동전략 상세내용"
		);

		int threadCount = 99;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					RestAssured.

						given()
						.contentType(ContentType.JSON)
						.header("Authorization", "Bearer " + authToken)
						.body(requestDto).

						when()
						.post(baseUrl + "/initiative").

						then()
						.statusCode(HttpStatus.CREATED.value())
						.extract().body().asString();
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();

		Project project = em.createQuery(
				"select p from Project p where p.id = :id", Project.class)
			.setParameter("id", 9999L)
			.getSingleResult();

		KeyResult keyResult = em.createQuery(
				"select k from KeyResult k where k.id = :id", KeyResult.class)
			.setParameter("id", 9999L)
			.getSingleResult();

		assertThat(keyResult.getInitiative().size()).isEqualTo(100);
		assertThat(project.getProgress()).isEqualTo(1.0);
	}

	@Test
	void 행동전략_완료시_기대하는_응답을_리턴한다() throws Exception {
		String initiativeToken = "ini_ixYjj5nODa3sdA12";
		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + authToken).

			when()
			.put(baseUrl + "/initiative/" + initiativeToken + "/done").

			then()
			.statusCode(HttpStatus.OK.value())
			.extract().body().asString();

		assertThat(response).isEqualTo("ini_ixYjj5nODa3sdA12");
	}

	@Test
	void 핵심결과토큰으로_행동전략_리스트_조회시_기대하는_응답을_리턴한다() throws Exception {
		String keyResultToken = "key_wV6MX15WQ3DTzQMs";

		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + authToken).

			when()
			.get(baseUrl + "/initiative/list/" + keyResultToken).

			then()
			.statusCode(HttpStatus.OK.value())
			.extract().body().asString();

	}

	@Test
	void 팀원의_행동전략에_피드백을_추가하면_기대하는_응답을_리턴한다() throws Exception {

		final String response = RestAssured.

			given()
			.header("Authorization", "Bearer " + authToken)
			.contentType(ContentType.JSON)
			.body(new FeedbackSaveRequest("피드백 작성", "GOOD_IDEA", "project-fgFHxGWeIUQt", "ini_ixYjj5nODqtb3AH8")).

			when()
			.post(baseUrl).

			then()
			.statusCode(HttpStatus.CREATED.value())
			.extract().body().asString();

		assertThat(response).containsPattern(
			Pattern.compile("feedback-[a-zA-Z0-9]{11}"));

	}
}



