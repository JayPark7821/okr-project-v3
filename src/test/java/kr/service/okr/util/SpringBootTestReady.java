package kr.service.okr.util;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import io.restassured.RestAssured;

@Import(TestConfig.class)
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringBootTestReady {

	@LocalServerPort
	int port;
	@Autowired
	DatabaseCleanup databaseCleanup;
	@Autowired
	public DataLoader dataLoader;

	@Value("${app.auth.refreshTokenRegenerationThreshold}")
	public Long 토큰_유효기간_임계값;

	@Value("${app.auth.tokenSecret}")
	public String key;

	@Value("${app.auth.tokenExpiry}")
	public Long 엑세스_토큰_유효기간_임계값;

	public void setUp() {
		databaseCleanup.execute();
		RestAssured.port = port;
	}
}
