package kr.jay.okrver3.interfaces.notification;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.sql.Connection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.junit.jupiter.api.AfterAll;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.transaction.annotation.Transactional;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import kr.jay.okrver3.common.utils.JwtTokenUtils;
import kr.jay.okrver3.interfaces.notification.response.NotificationResponse;
import kr.jay.okrver3.util.DatabaseCleanup;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NotificationApiControllerAcceptanceTest {

	@Value("${app.auth.tokenSecret}")
	private String key;

	@Value("${app.auth.tokenExpiry}")
	private Long accessExpiredTimeMs;

	@PersistenceContext
	EntityManager em;

	@Autowired
	DataSource dataSource;
	private static final String baseUrl = "/api/v1/notification";
	@LocalServerPort
	private int port;

	@Autowired
	private DatabaseCleanup databaseCleanup;

	private String authToken;

	@BeforeAll
	void setUpAll() {
		try (Connection conn = dataSource.getConnection()) {
			authToken = JwtTokenUtils.generateToken("notificationTest@naver.com", key, accessExpiredTimeMs);
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/insert-project-data.sql"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterAll
	void afterAll() {
		databaseCleanup.execute();
	}

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@Test
	void getNotifications을_호출화면_기대하는_응답을_리턴한다() throws Exception {
		List<String> notificationTokens = List.of("noti_aaaaaMoZey1SERx", "noti_e144441Zey1SERx");
		final List<NotificationResponse> response = RestAssured.

			given()
			.header("Authorization", "Bearer " + authToken)
			.contentType(ContentType.JSON).

			when()
			.get(baseUrl).

			then()
			.statusCode(HttpStatus.OK.value())
			.extract().body().jsonPath()
			.getList("content", NotificationResponse.class);

		assertThat(response.size()).isEqualTo(2);
		for (int i = 0; i < response.size(); i++) {
			NotificationResponse r = response.get(i);
			assertThat(r.notiToken()).isEqualTo(notificationTokens.get(i));
		}
	}
}
