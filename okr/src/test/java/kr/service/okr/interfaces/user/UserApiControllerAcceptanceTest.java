package kr.service.okr.interfaces.user;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.transaction.annotation.Transactional;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import kr.service.okr.util.TestConfig;
import kr.service.okr.common.exception.ErrorCode;
import kr.service.okr.common.utils.JwtTokenUtils;
import kr.service.okr.infrastructure.user.auth.TokenVerifier;

@Import(TestConfig.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApiControllerAcceptanceTest {

	@LocalServerPort
	private int port;

	@Value("${app.auth.tokenSecret}")
	private String key;

	@Value("${app.auth.refreshTokenRegenerationThreshold}")
	private Long refreshTokenRegenerationThreshold;

	@Autowired
	DataSource dataSource;
	@Autowired
	private TokenVerifier tokenVerifier;
	private static final String PROVIDER_APPLE = "APPLE";
	private static final String NOT_MEMBER_APPLE_ID_TOKEN = "notMemberIdToken";
	private static final String APPLE_ID_TOKEN = "appleToken";
	private static final String GOOGLE_ID_TOKEN = "googleToken";
	private String availAccessToken ;
	private String expiredAccessToken ;
	private static final String baseUrl = "/api/v1/user";



	@BeforeAll
	void setUpAll() {

		try (Connection conn = dataSource.getConnection()) {

			availAccessToken = JwtTokenUtils.generateToken("apple@apple.com", key, refreshTokenRegenerationThreshold + 10000000L);
			expiredAccessToken = JwtTokenUtils.generateToken("fakeAppleEmail", key, refreshTokenRegenerationThreshold - 10000000L);
			String sql = "insert into refresh_token (refresh_token_seq, user_email, refresh_token) "
						+ "values ('9999', 'apple@apple.com', ?) ,"
								+ "('9998', 'fakeAppleEmail', ? )";

			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, availAccessToken);
			statement.setString(2, expiredAccessToken);
			statement.executeUpdate();

			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/insert-user.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/insert-project.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/insert-team.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/insert-keyresult.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/insert-initiative.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/insert-guest.sql"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}


	@Test
	void refreshToken으로_getNewAccessToken을_호출하면_기대하는_응답을_리턴한다_new_accessToken() {

		final JsonPath response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + availAccessToken).

			when()
			.get("/api/v1/user/refresh").

			then()
			.statusCode(HttpStatus.OK.value())
			.extract().jsonPath();

		assertThat(response.getString("accessToken")).isNotNull();
		assertThat(response.getString("refreshToken")).isEqualTo(availAccessToken);
	}

	@Test
	void refreshToken이_기간이_설정값_이하일때_getNewAccessToken을_호출하면_기대하는_응답을_리턴한다_new_accessToken() {

		final JsonPath response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + expiredAccessToken).

			when()
			.get("/api/v1/user/refresh").

			then()
			.statusCode(HttpStatus.OK.value())
			.extract().jsonPath();

		assertThat(response.getString("accessToken")).isNotNull();
		assertThat(response.getString("refreshToken")).isNotEqualTo(expiredAccessToken);
	}


	@Test
	void 등록안된_jobCategory로_getJobField를_호출하면_기대하는_응답_exception_반환한다() throws Exception {

		String category = "BACK_ENDDDD";
		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON).

			when()
			.get(baseUrl + "/job/"+ category + "/fields").

			then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.extract().body().asString();

		assertThat(response).isEqualTo(ErrorCode.INVALID_JOB_CATEGORY.getMessage());
	}

	@Test
	void 등록안된_jobField로_getJobCategoryBy를_호출하면_기대하는_응답_exception_반환한다() throws Exception {

		String jobField = "WEB_FRONT_END_DEVELOPERRRRR";
		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON).

			when()
			.get(baseUrl + "/job/field/"+ jobField).

			then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.extract().body().asString();

		assertThat(response).isEqualTo(ErrorCode.INVALID_JOB_DETAIL_FIELD.getMessage());
	}


}
