package kr.service.okr.acceptance.project.initiative;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.service.okr.project.api.RegisterInitiativeRequest;

public class InitiativeAcceptanceTestSteps {
	private static final String baseUrl = "/api/v1/initiative";

	static ExtractableResponse<Response> 횅동전략_추가_요청(
		RegisterInitiativeRequest 행동전략_생성_데이터,
		String 로그인_유저_인증_토큰
	) throws Exception {
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

	static void 행동전략_추가_요청_99건_동시성_테스트(
		RegisterInitiativeRequest 행동전략_생성_데이터,
		String 로그인_유저_인증_토큰
	) throws Exception {

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

}
