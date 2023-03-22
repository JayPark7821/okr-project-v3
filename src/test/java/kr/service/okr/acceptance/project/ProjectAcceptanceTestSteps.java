package kr.service.okr.acceptance.project;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.service.okr.interfaces.project.request.ProjectSaveRequest;

public class ProjectAcceptanceTestSteps {

	private static final String baseUrl = "/api/v1/project";

	static ExtractableResponse<Response> 프로젝트_생성_요청(ProjectSaveRequest 프로젝트_생성_데이터, String 로그인_유저_인증_토큰) throws
		Exception {
		return RestAssured.

			given().log().all()
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰)
			.contentType(ContentType.JSON)
			.body(프로젝트_생성_데이터).

			when()
			.post(baseUrl).

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
			.get(baseUrl + "/participate").

			then()
			.log().all()
			.extract();
	}

	static ExtractableResponse<Response> 프로젝트_조회_요청(String 프로젝트_토큰, String 로그인_유저_인증_토큰) {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰).

			when()
			.get(baseUrl + "/" + 프로젝트_토큰).

			then()
			.log().all()
			.extract();
	}

	static ExtractableResponse<Response> 메인_페이지_프로젝트_조회_요청(
		String 정렬순서,
		String 종료된_프로젝트_포함여부,
		String 팀_타입,
		String 로그인_유저_인증_토큰
	) {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰).

			when()
			.get(baseUrl + "/" + "?" + "sortType=" + 정렬순서 + "&" + "includeFinishedProjectYN=" + 종료된_프로젝트_포함여부 + "&"
				+ "projectType=" + 팀_타입).

			then()
			.log().all()
			.extract();
	}

	static ExtractableResponse<Response> 프로젝트_사이드_메뉴_조회_요청(String 프로젝트_토큰, String 로그인_유저_인증_토큰) {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰).

			when()
			.get(baseUrl + "/" + 프로젝트_토큰 + "/side").

			then()
			.log().all()
			.extract();
	}
}
