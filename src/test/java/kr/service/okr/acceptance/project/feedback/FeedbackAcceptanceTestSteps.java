package kr.service.okr.acceptance.project.feedback;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.service.okr.interfaces.project.request.FeedbackSaveRequest;

public class FeedbackAcceptanceTestSteps {

	private static final String baseUrl = "/api/v1/feedback";

	static ExtractableResponse<Response> 피드백_추가_요청(String 행동전략_토큰, String 피드백_내용, String 피드백_이모티콘,
		String 로그인_유저_인증_토큰) throws
		Exception {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰)
			.body(new FeedbackSaveRequest(피드백_내용, 피드백_이모티콘, 행동전략_토큰)).

			when()
			.post(baseUrl).

			then()
			.log().all()
			.extract();
	}

	static ExtractableResponse<Response> 행동전략에_등록된_피드백_조회_요청(String 행동전략_토큰, String 로그인_유저_인증_토큰) throws
		Exception {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰).

			when()
			.get(baseUrl + "/" + 행동전략_토큰).

			then()
			.log().all()
			.extract();
	}

	static ExtractableResponse<Response> 피드백_남기지_않은_행동전략_카운트_조회_요청(String 로그인_유저_인증_토큰) throws
		Exception {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰).

			when()
			.get(baseUrl + "/count").

			then()
			.log().all()
			.extract();
	}

	static ExtractableResponse<Response> 받은_피드백_조회_요청(String 검색범위, String 로그인_유저_인증_토큰) throws
		Exception {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰).

			when()
			.get(baseUrl + "?searchRange=" + 검색범위).

			then()
			.log().all()
			.extract();
	}

	static ExtractableResponse<Response> 피드백_상태_업데이트_요청(String 피드백_토큰, String 로그인_유저_인증_토큰) throws
		Exception {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰).

			when()
			.get(baseUrl + "/" + 피드백_토큰).

			then()
			.log().all()
			.extract();
	}

	static ExtractableResponse<Response> 피드백을_남겨야_하는_행동전략_조회_요청(String 로그인_유저_인증_토큰) throws
		Exception {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰).

			when()
			.get(baseUrl + "/required").

			then()
			.log().all()
			.extract();
	}

}
