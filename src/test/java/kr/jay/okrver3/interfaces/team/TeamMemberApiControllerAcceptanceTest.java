package kr.jay.okrver3.interfaces.team;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import kr.jay.okrver3.TestConfig;
import kr.jay.okrver3.common.utils.JwtTokenUtils;

@Sql("classpath:insert-user.sql")
@Import(TestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamMemberApiControllerAcceptanceTest {

	@Value("${app.auth.tokenSecret}")
	private String key;

	@Value("${app.auth.tokenExpiry}")
	private Long accessExpiredTimeMs;
	@LocalServerPort
	private int port;

	private final String baseUrl = "/api/v1/team";

	private String authToken;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
		authToken = JwtTokenUtils.generateToken("apple@apple.com", key, accessExpiredTimeMs);
	}

	@Test
	@DisplayName("팀원 추가를 시도하면 기대하는 응답(추가된 email주소)을 반환한다.")
	void invite_team_member() throws Exception {
		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + authToken)
			.body(new TeamMemberInviteRequestDto("project-fgFHxGWeIUQt", "test@gmail.com")).

			when()
			.post(baseUrl + "/invite").

			then()
			.statusCode(HttpStatus.CREATED.value())
			.extract().body().asString();

		assertThat(response).isEqualTo("test@gmail.com");
	}
}
