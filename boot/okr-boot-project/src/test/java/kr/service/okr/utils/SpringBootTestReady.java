package kr.service.okr.utils;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import io.restassured.RestAssured;

@Import(TestConfig.class)
@ActiveProfiles("test")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = WireMockServerConfig.class)
public class SpringBootTestReady {

	@LocalServerPort
	int port;

	public static final int PORT = 8080;

	@Autowired
	public DataLoader dataLoader;

	public void setUp() {
		RestAssured.port = port;
	}
}
