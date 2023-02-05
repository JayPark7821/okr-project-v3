package kr.jay.okrver3;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApiControllerAcceptanceTest {


	@LocalServerPort
	private int port;

	private static final String PROVIDER = "LOCAL";
	private static final String ID_TOKEN = "test-token";

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@Test
	@DisplayName("가입한 유저 정보가 없을 때  idToken을 통해 로그인을 시도하면 기대하는 응답(Guest)을 반환한다.")
	void login_With_IdToken_when_before_join() throws Exception {
		final JsonPath response = RestAssured.

			given()
			.contentType(ContentType.JSON).

			when()
			.post("/api/v1/user/login/" + PROVIDER + "/" + ID_TOKEN).

			then()
			.statusCode(201)
			.extract().jsonPath();

		assertThat(response).hasNoNullFieldsOrPropertiesExcept(
			"roleType",
			"jobFieldDetail",
			"accessToken",
			"refreshToken"
		);
		assertThat(response.getString("guestUuid")).isNotNull();
	}

	@Test
	@DisplayName("가입한 유저 정보가 있을 때  idToken을 통해 로그인을 시도하면 기대하는 응답(Tokens)을 반환한다.")
	void login_With_IdToken_when_after_join() throws Exception {

		final JsonPath response = RestAssured.

			given()
			.contentType(ContentType.JSON).

			when()
			.post("/api/v1/user/login/" + PROVIDER + "/" + ID_TOKEN).

			then()
			.statusCode(201)
			.extract().jsonPath();


		assertThat(response.getString("guestId")).isNull();
		assertThat(response.getString("email")).isNotNull();
		assertThat(response.getString("name")).isNotNull();
		assertThat(response.getString("providerType")).isNotNull();
		assertThat(response.getString("accessToken")).isNotNull();
		assertThat(response.getString("refreshToken")).isNotNull();

	}

}
