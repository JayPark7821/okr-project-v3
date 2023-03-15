package kr.jay.okrver3.acceptance.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class UserAcceptanceTestSteps {

	private static final String baseUrl = "/api/v1/user";

	public static ExtractableResponse<Response> 소셜_idToken으로_로그인_요청(String 소셜플랫폼, String idToken) throws Exception {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON).

			when()
			.post(baseUrl + "/login/" + 소셜플랫폼 + "/" + idToken).

			then()
			.log().all()
			.extract();
	}


}
