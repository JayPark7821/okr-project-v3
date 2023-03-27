package kr.service.okr.acceptance.project.initiative;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.service.okr.interfaces.project.request.ProjectInitiativeSaveRequest;

public class InitiativeAcceptanceTestSteps {

	private static final String baseUrl = "/api/v1/initiative";

	static ExtractableResponse<Response> 횅동전략_추가_요청(ProjectInitiativeSaveRequest 행동전략_생성_데이터,
		String 로그인_유저_인증_토큰) throws
		Exception {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰)
			.body(행동전략_생성_데이터).

			when()
			.post(baseUrl).

			then()
			.log().all()
			.extract();
	}

	static void 행동전략_추가_요청_동시성_테스트(ProjectInitiativeSaveRequest 행동전략_생성_데이터, String 로그인_유저_인증_토큰) throws
		Exception {

		int threadCount = 99;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					RestAssured.

						given()
						.contentType(ContentType.JSON)
						.header("Authorization", "Bearer " + 로그인_유저_인증_토큰)
						.body(행동전략_생성_데이터).

						when()
						.post(baseUrl).

						then()
						.statusCode(HttpStatus.CREATED.value())
						.extract().body().asString();
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();
	}

	static ExtractableResponse<Response> 행동전략_완료_요청(String 완료_처리할_행동전략_토큰, String 로그인_유저_인증_토큰) throws
		Exception {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰).

			when()
			.put(baseUrl + "/" + 완료_처리할_행동전략_토큰 + "/done").

			then()
			.log().all()
			.extract();
	}

	static ExtractableResponse<Response> 행동전략_리스트_조회_요청(String 행동전략_조회용_핵심결과_토큰, String 로그인_유저_인증_토큰) throws
		Exception {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰).

			when()
			.get(baseUrl + "/list/" + 행동전략_조회용_핵심결과_토큰).

			then()
			.log().all()
			.extract();
	}

	static ExtractableResponse<Response> 행동전략_상세_조회_요청(String 행동전략_상세_조회용_토큰, String 로그인_유저_인증_토큰) throws
		Exception {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰).

			when()
			.get(baseUrl + "/" + 행동전략_상세_조회용_토큰).

			then()
			.log().all()
			.extract();
	}

	static ExtractableResponse<Response> 날짜로_행동전략_조회_요청(String 행동전략_조회_날짜, String 로그인_유저_인증_토큰) throws
		Exception {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰).

			when()
			.get(baseUrl + "/date/" + 행동전략_조회_날짜).

			then()
			.log().all()
			.extract();
	}

	static ExtractableResponse<Response> 년월로_행동전략_진행중인_일자_리스트_조회_요청(String 행동전략_조회_년월, String 로그인_유저_인증_토큰) throws
		Exception {
		return RestAssured.

			given().log().all()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + 로그인_유저_인증_토큰).

			when()
			.get(baseUrl + "/yearmonth/" + 행동전략_조회_년월).

			then()
			.log().all()
			.extract();
	}

}
