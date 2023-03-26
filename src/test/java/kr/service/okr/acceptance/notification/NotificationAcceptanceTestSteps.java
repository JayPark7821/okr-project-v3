package kr.service.okr.acceptance.notification;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class NotificationAcceptanceTestSteps {

	private static final String baseUrl = "/api/v1/notification";

	static ExtractableResponse<Response> 알림_조회_요청(String 로그인_유저_인증_토큰) throws Exception {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰).

			when()
			.get(baseUrl).

			then()
			.log().all()
			.extract();
	}

}
