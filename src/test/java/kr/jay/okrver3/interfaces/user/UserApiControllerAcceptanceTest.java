package kr.jay.okrver3.interfaces.user;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.sql.Connection;

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
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.transaction.annotation.Transactional;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import kr.jay.okrver3.TestConfig;
import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.utils.JwtTokenUtils;
import kr.jay.okrver3.domain.token.RefreshToken;
import kr.jay.okrver3.infrastructure.user.auth.OAuth2UserInfo;
import kr.jay.okrver3.infrastructure.user.auth.TokenVerifier;
import kr.jay.okrver3.interfaces.user.request.JoinRequest;

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

	@PersistenceContext
	EntityManager em;

	@Autowired
	DataSource dataSource;

	@Autowired
	private TokenVerifier tokenVerifier;
	private static final String PROVIDER_APPLE = "APPLE";
	private static final String NOT_MEMBER_ID_TOKEN = "notMemberIdToken";
	private static final String ID_TOKEN = "appleToken";
	private static final String DIF_ID_TOKEN = "googleToken";




	@BeforeAll
	void setUpAll() {
		try (Connection conn = dataSource.getConnection()) {
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
	@DisplayName("가입한 유저 정보가 없을 때  idToken을 통해 로그인을 시도하면 기대하는 응답(Guest without token)을 반환한다.")
	void login_With_IdToken_when_before_join() throws Exception {
		final JsonPath response = RestAssured.

			given()
			.contentType(ContentType.JSON).

			when()
			.post("/api/v1/user/login/" + PROVIDER_APPLE + "/" + NOT_MEMBER_ID_TOKEN).

			then()
			.statusCode(HttpStatus.OK.value())
			.extract().jsonPath();

		assertResponseWithFixture(response,"notMemberIdToken");
	}

	@Test
	@DisplayName("가입한 유저 정보가 있을 때  idToken을 통해 로그인을 시도하면 기대하는 응답(with token)을 반환한다.")
	void login_With_IdToken_when_after_join() throws Exception {

		final JsonPath response = RestAssured.

			given()
			.contentType(ContentType.JSON).

			when()
			.post("/api/v1/user/login/" + PROVIDER_APPLE + "/" + ID_TOKEN).

			then()
			.statusCode(HttpStatus.OK.value())
			.extract().jsonPath();

		assertLoginUser(response);

	}

	@Test
	@DisplayName("가입한 유저 정보와 다른 ProviderType으로 로그인을 호출하면 기대하는 예외를 던진다.")
	void loginWithSocialIdToken_when_after_join_and_with_another_provider() {
		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON).

			when()
			.post("/api/v1/user/login/" + PROVIDER_APPLE + "/" + DIF_ID_TOKEN).

			then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.extract().body().asString();

		assertThat(response).isEqualTo("소셜 provider 불일치, GOOGLE(으)로 가입한 계정이 있습니다.");
	}

	@Test
	@DisplayName("게스트 정보가 있을 때 join()을 호출하면 기대하는 응답을 반환한다.")
	void join_after_guest_login() {

		final JsonPath response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.body(new JoinRequest("guest-rkmZUIUNWk3333", "guest", "guestJoinTest@email.com", "WEB_SERVER_DEVELOPER")).

			when()
			.post("/api/v1/user/join").

			then()
			.statusCode(HttpStatus.CREATED.value())
			.extract().jsonPath();

		assertLoginUser(response);

	}

	@Test
	@DisplayName("게스트 정보가 없을 때 join()을 호출하면 기대하는 예외를 던진다.")
	void join_before_guest_login() {
		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.body(new JoinRequest("not-registered-guest-id", "guest", "guest@email.com", "Developer")).

			when()
			.post("/api/v1/user/join").

			then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.extract().body().asString();

		assertThat(response).isEqualTo(ErrorCode.INVALID_JOIN_INFO.getMessage());
	}

	@Test
	// @Sql({"classpath:insert-user.sql", "classpath:insert-guest.sql"})
	@DisplayName("가입한 유저 정보가 있을 때 join()을 호출하면 기대하는 예외를 던진다.")
	void join_again_when_after_join() {
		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.body(new JoinRequest("guest-rkmZUIUNWkSMX3", "appleUser", "guest@email.com", "WEB_SERVER_DEVELOPER")).

			when()
			.post("/api/v1/user/join").

			then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.extract().body().asString();

		assertThat(response).isEqualTo(ErrorCode.ALREADY_JOINED_USER.getMessage());
	}

	@Test
	void refreshToken으로_getNewAccessToken을_호출하면_기대하는_응답을_리턴한다_new_accessToken() {

		String accessToken = JwtTokenUtils.generateToken("apple@apple.com", key, 10000000000000L);
		RefreshToken entity = new RefreshToken("apple@apple.com", accessToken);
		em.persist(entity);

		Long refreshTokenSeq = entity.getRefreshTokenSeq();
		System.out.println("refreshTokenSeq = " + refreshTokenSeq);

		final JsonPath response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + accessToken).

			when()
			.get("/api/v1/user/refresh").

			then()
			.statusCode(HttpStatus.OK.value())
			.extract().jsonPath();

		assertThat(response.getString("accessToken")).isNotEqualTo(accessToken);
	}

	// TODO :: refresh token 3일 이하 남았을때 refresh token 재발급 테스트
	private void assertLoginUser(JsonPath response) {
		assertThat(response.getString("guestId")).isNull();
		assertThat(response.getString("email")).isNotNull();
		assertThat(response.getString("name")).isNotNull();
		assertThat(response.getString("providerType")).isNotNull();
		assertThat(response.getString("accessToken")).isNotNull();
		assertThat(response.getString("refreshToken")).isNotNull();
		assertThat(response.getString("jobFieldDetail")).isNotNull();
	}

	private void assertResponseWithFixture(JsonPath response, String fixture) {

		OAuth2UserInfo info = tokenVerifier.verifyIdToken(fixture);

		assertThat(response.getString("guestId")).isNotNull();
		assertThat(response.getString("email")).isEqualTo(info.email());
		assertThat(response.getString("name")).isEqualTo(info.name());
		assertThat(response.getString("providerType")).isEqualTo(info.providerType().name());
		assertThat(response.getString("accessToken")).isNull();
		assertThat(response.getString("refreshToken")).isNull();
		assertThat(response.getString("jobFieldDetail")).isNull();
	}

}
