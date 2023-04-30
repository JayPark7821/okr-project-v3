package kr.service.okr.acceptance.project.keyresult;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.service.okr.project.api.RegisterKeyResultRequest;
import kr.service.okr.project.api.UpdateKeyResultRequest;

public class KeyResultAcceptanceTestSteps {

	private static final String baseUrl = "/api/v1/keyresult";

	static ExtractableResponse<Response> 핵심결과_추가_요청(String 프로젝트_토큰, String 핵심결과, String 로그인_유저_인증_토큰) throws
		Exception {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰)
			.body(new RegisterKeyResultRequest(프로젝트_토큰, 핵심결과)).

			when()
			.post(baseUrl).

			then()
			.log().all()
			.extract();
	}

	static ExtractableResponse<Response> 핵심결과_수정_요청(String 프로젝트_토큰, String 핵심결과, String 로그인_유저_인증_토큰) throws
		Exception {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰)
			.body(new UpdateKeyResultRequest(프로젝트_토큰, 핵심결과)).

			when()
			.put(baseUrl).

			then()
			.log().all()
			.extract();
	}

}
