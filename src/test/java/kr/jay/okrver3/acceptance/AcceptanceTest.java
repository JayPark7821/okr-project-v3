package kr.jay.okrver3.acceptance;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import io.restassured.RestAssured;
import kr.jay.okrver3.common.utils.JwtTokenUtils;
import kr.jay.okrver3.util.DataLoader;
import kr.jay.okrver3.util.DatabaseCleanup;
import kr.jay.okrver3.util.TestConfig;

@Import(TestConfig.class)
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

	@LocalServerPort
	int port;

	@Value("${app.auth.tokenSecret}")
	String key;

	@Value("${app.auth.tokenExpiry}")
	Long accessExpiredTimeMs;

	@Autowired
	DatabaseCleanup databaseCleanup;

	@Autowired
	DataLoader dataLoader;

	public static String 로그인_유저;

	@BeforeEach
	public void setUp() {
		databaseCleanup.execute();
		dataLoader.loadData();
		RestAssured.port = port;

		로그인_유저 = JwtTokenUtils.generateToken("apple@apple.com", key, accessExpiredTimeMs);
	}
}
