package kr.jay.okrver3.acceptance.user;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.jay.okrver3.domain.user.JobCategory;
import kr.jay.okrver3.domain.user.JobField;
import kr.jay.okrver3.interfaces.user.request.JoinRequest;
import kr.jay.okrver3.interfaces.user.request.UserInfoUpdateRequest;

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

	public static ExtractableResponse<Response> 회원가입_요청(JoinRequest 회원가입_정보) {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.body(회원가입_정보).

			when()
			.post(baseUrl + "/join").

			then()
			.log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 프로잭트_생성_전_email_검증_요청(String 검증할_이메일주소, String 로그인_유저_인증_토큰) throws
		Exception {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰).

			when()
			.get(baseUrl + "/validate" + "/" + 검증할_이메일주소).

			then()
			.log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 직업_카테고리_조회_요청() throws Exception {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON).

			when()
			.get(baseUrl + "/job/category").

			then()
			.log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 직업_조회_요청(JobCategory 직업_카테고리) throws Exception {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON).

			when()
			.get(baseUrl + "/job/" + 직업_카테고리.getCode() + "/fields").

			then()
			.log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 직업_으로_직업_카테고리_조회_요청(JobField 직업) throws Exception {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON).

			when()
			.get(baseUrl + "/job/field/" + 직업.getCode()).

			then()
			.log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 로그인_유저_정보_요청(String 로그인_유저_인증_토큰) throws Exception {
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

	public static ExtractableResponse<Response> 로그인_유저_정보_수정_요청(String 수정할_이름, JobField 수정할_직업,
		String 로그인_유저_인증_토큰) throws Exception {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰)
			.body(new UserInfoUpdateRequest(수정할_이름, "profileImage", 수정할_직업.getCode())).

			when()
			.put(baseUrl).

			then()
			.log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 새로운_인증_토큰_발급_요청(String 로그인_유저_인증_토큰) throws Exception {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰).

			when()
			.put(baseUrl + "/refresh").

			then()
			.log().all()
			.extract();
	}

}
