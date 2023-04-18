package kr.service.okr.acceptance.project;

import static java.nio.charset.Charset.*;
import static org.springframework.util.StreamUtils.*;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.service.okr.project.api.RegisterProjectRequestDto;

public class ProjectAcceptanceTestSteps {

	private static final String baseUrl = "/api/v1/project";

	static ExtractableResponse<Response> 프로젝트_생성_요청(RegisterProjectRequestDto 프로젝트_생성_데이터) throws
		Exception {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.body(프로젝트_생성_데이터).

			when()
			.post(baseUrl).

			then()
			.log().all()
			.extract();
	}

	static ExtractableResponse<Response> 프로젝트_조회_요청(String 프로젝트_토큰, String 로그인_인증_토큰,
		WireMockServer wireMockServer) throws
		Exception {

		mockUserApi(wireMockServer);

		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + 로그인_인증_토큰).

			when()
			.get(baseUrl + "/" + 프로젝트_토큰).

			then()
			.log().all()
			.extract();
	}

	static void mockUserApi(WireMockServer mockService) throws IOException {

		mockService.stubFor(WireMock.get(WireMock.urlEqualTo("/api/v1/user/auth"))
			.willReturn(WireMock.aResponse()
				.withStatus(HttpStatus.OK.value())
				.withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
				.withBody(
					copyToString(
						ProjectAcceptanceTestSteps.class.getClassLoader()
							.getResourceAsStream("payload/get-user-response.json"),
						defaultCharset()))));

	}

}
