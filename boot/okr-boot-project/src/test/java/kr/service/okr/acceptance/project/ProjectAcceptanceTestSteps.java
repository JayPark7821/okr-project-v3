package kr.service.okr.acceptance.project;

import org.springframework.http.HttpHeaders;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.service.okr.project.api.RegisterProjectRequestDto;

public class ProjectAcceptanceTestSteps {

	private static final String baseUrl = "/api/v1/project";

	static ExtractableResponse<Response> 프로젝트_생성_요청(RegisterProjectRequestDto 프로젝트_생성_데이터, String 로그인_유저_인증_토큰) throws
		Exception {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰)
			.body(프로젝트_생성_데이터).

			when()
			.post(baseUrl).

			then()
			.log().all()
			.extract();
	}

	static ExtractableResponse<Response> 프로젝트_조회_요청(String 프로젝트_토큰, String 로그인_인증_토큰) throws
		Exception {

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

}
