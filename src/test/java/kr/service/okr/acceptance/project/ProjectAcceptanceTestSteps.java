package kr.service.okr.acceptance.project;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.service.okr.interfaces.project.request.ProjectSaveRequest;

public class ProjectAcceptanceTestSteps {

	private static final String baseUrl = "/api/v1";

	static ExtractableResponse<Response> 프로젝트_생성_요청(ProjectSaveRequest 프로젝트_생성_데이터, String 로그인_유저_인증_토큰) throws
		Exception {
		return RestAssured.

			given().log().all()
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰)
			.contentType(ContentType.JSON)
			.body(프로젝트_생성_데이터).

			when()
			.post(baseUrl + "/project").

			then()
			.log().all()
			.extract();
	}

	static ExtractableResponse<Response> 회원탈퇴_요청전_참여중인_프로젝트_조회(String 로그인_유저_인증_토큰) {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰).

			when()
			.get(baseUrl + "/project/participate").

			then()
			.log().all()
			.extract();
	}

}
