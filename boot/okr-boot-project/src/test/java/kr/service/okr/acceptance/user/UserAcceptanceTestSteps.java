package kr.service.okr.acceptance.user;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class UserAcceptanceTestSteps {

	private static final String baseUrl = "/api/v1/user";

	static ExtractableResponse<Response> 로그인_요청(String 소셜타입, String 소셜토큰) throws
		Exception {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON).

			when()
			.post(baseUrl + "/login" + "/" + 소셜타입 + "/" + 소셜토큰).

			then()
			.log().all()
			.extract();
	}

}
