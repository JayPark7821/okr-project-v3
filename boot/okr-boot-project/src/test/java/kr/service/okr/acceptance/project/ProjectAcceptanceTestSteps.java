package kr.service.okr.acceptance.project;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.service.okr.api.RegisterProjectRequestDto;

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

}
