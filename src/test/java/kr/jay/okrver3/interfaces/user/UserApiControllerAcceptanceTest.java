package kr.jay.okrver3.interfaces.user;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import kr.jay.okrver3.TestConfig;
import kr.jay.okrver3.common.exception.ErrorCode;

@Import(TestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApiControllerAcceptanceTest {

	@LocalServerPort
	private int port;

	private static final String PROVIDER = "APPLE";
	private static final String ID_TOKEN = "idToken";
	private static final String DIF_ID_TOKEN = "googleToken";

	private static final String DIF_PROVIDER = "GOOGLE";

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
			.post("/api/v1/user/login/" + PROVIDER + "/" + ID_TOKEN).

			then()
			.statusCode(HttpStatus.OK.value())
			.extract().jsonPath();

		assertGuset(response);
	}

	@Test
	@Sql("classpath:insert-user.sql")
	@DisplayName("가입한 유저 정보가 있을 때  idToken을 통해 로그인을 시도하면 기대하는 응답(with token)을 반환한다.")
	void login_With_IdToken_when_after_join() throws Exception {

		final JsonPath response = RestAssured.

			given()
			.contentType(ContentType.JSON).

			when()
			.post("/api/v1/user/login/" + DIF_PROVIDER + "/" + ID_TOKEN).

			then()
			.statusCode(HttpStatus.OK.value())
			.extract().jsonPath();

		assertLoginUser(response);

	}

	@Test
	@Sql("classpath:insert-different-social-google-user.sql")
	@DisplayName("가입한 유저 정보와 다른 ProviderType으로 로그인을 호출하면 기대하는 예외를 던진다.")
	void loginWithSocialIdToken_when_after_join_and_with_another_provider() {
		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON).

			when()
			.post("/api/v1/user/login/" + PROVIDER + "/" + DIF_ID_TOKEN).

			then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.extract().body().asString();

		assertThat(response).isEqualTo("소셜 provider 불일치, APPLE(으)로 가입한 계정이 있습니다.");
	}

	@Test
	@Sql("classpath:insert-guest.sql")
	@DisplayName("게스트 정보가 있을 때 join()을 호출하면 기대하는 응답을 반환한다.")
	void join_after_guest_login() {

		final JsonPath response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.body(new JoinRequestDto("guest-rkmZUIUNWkSMX3", "guest", "guest@email.com", "WEB_SERVER_DEVELOPER")).

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
			.body(new JoinRequestDto("not-registered-guest-id", "guest", "guest@email.com", "Developer")).

			when()
			.post("/api/v1/user/join").

			then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.extract().body().asString();

		assertThat(response).isEqualTo(ErrorCode.INVALID_JOIN_INFO.getMessage());
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-guest.sql"})
	@DisplayName("가입한 유저 정보가 있을 때 join()을 호출하면 기대하는 예외를 던진다.")
	void join_again_when_after_join() {
		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.body(new JoinRequestDto("guest-rkmZUIUNWkSMX3", "appleUser", "guest@email.com", "WEB_SERVER_DEVELOPER")).

			when()
			.post("/api/v1/user/join").

			then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.extract().body().asString();

		assertThat(response).isEqualTo(ErrorCode.ALREADY_JOINED_USER.getMessage());
	}

	private void assertLoginUser(JsonPath response) {
		assertThat(response.getString("guestId")).isNull();
		assertThat(response.getString("email")).isNotNull();
		assertThat(response.getString("name")).isNotNull();
		assertThat(response.getString("providerType")).isNotNull();
		assertThat(response.getString("accessToken")).isNotNull();
		assertThat(response.getString("refreshToken")).isNotNull();
		assertThat(response.getString("jobFieldDetail")).isNotNull();
	}

	private void assertGuset(JsonPath response) {
		assertThat(response.getString("guestId")).isNotNull();
		assertThat(response.getString("email")).isNotNull();
		assertThat(response.getString("name")).isNotNull();
		assertThat(response.getString("providerType")).isNotNull();
		assertThat(response.getString("accessToken")).isNull();
		assertThat(response.getString("refreshToken")).isNull();
		assertThat(response.getString("jobFieldDetail")).isNull();
	}

}
