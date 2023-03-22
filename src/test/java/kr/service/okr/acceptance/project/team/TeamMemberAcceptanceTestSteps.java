package kr.service.okr.acceptance.project.team;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.service.okr.interfaces.project.request.TeamMemberInviteRequest;

public class TeamMemberAcceptanceTestSteps {

	private static final String baseUrl = "/api/v1/team";

	static ExtractableResponse<Response> 팀원_초대_가능여부_확인_요청(String 프로젝트_토큰, String 검증할_유저_이메일, String 로그인_유저_인증_토큰) throws
		Exception {
		return RestAssured.

			given().log().all()
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰)
			.contentType(ContentType.JSON).

			when()
			.get(baseUrl + "/invite/" + 프로젝트_토큰 + "/" + 검증할_유저_이메일).

			then()
			.log().all()
			.extract();
	}

	static ExtractableResponse<Response> 팀원_초대_요청(String 프로젝트_토큰, String 초대할_유저_이메일, String 로그인_유저_인증_토큰) throws
		Exception {
		return RestAssured.

			given().log().all()
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰)
			.contentType(ContentType.JSON)
			.body(new TeamMemberInviteRequest(프로젝트_토큰, 초대할_유저_이메일)).

			when()
			.post(baseUrl + "/invite").

			then()
			.log().all()
			.extract();
	}
}
