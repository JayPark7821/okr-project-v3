package kr.jay.okrver3.interfaces.team;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import kr.jay.okrver3.TestConfig;

@Import(TestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamMemberApiControllerAcceptanceTest {

	@LocalServerPort
	private int port;

	private final String baseUrl = "/api/v1/team";

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@Test
	@DisplayName("팀원 추가를 시도하면 기대하는 응답(추가된 email주소)을 반환한다.")
	void login_With_IdToken_when_before_join() throws Exception {
		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.body(new TeamMemberInviteRequestDto("project-fgFHxGWeIUQt", "test@gmail.com")).

			when()
			.post(baseUrl + "/invite").

			then()
			.statusCode(HttpStatus.CREATED.value())
			.extract().body().asString();

		assertThat(response).isEqualTo("test@gmail.com");
	}
}
