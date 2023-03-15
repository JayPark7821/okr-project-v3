package kr.jay.okrver3.acceptance.user;

import static kr.jay.okrver3.acceptance.user.UserAcceptanceTestSteps.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.jay.okrver3.acceptance.AcceptanceTest;
import kr.jay.okrver3.infrastructure.user.auth.OAuth2UserInfo;
import kr.jay.okrver3.infrastructure.user.auth.TokenVerifier;
import kr.jay.okrver3.interfaces.user.request.JoinRequest;

@DisplayName("User domain acceptance test")
public class UserAcceptanceTest extends AcceptanceTest {

	private static final String 애플 = "APPLE";
	private static final String 회원가입안된_애플_idToken = "notMemberIdToken";
	private static final String 회원가입된_애플_idToken = "appleToken";
	private static final String 회원가입된_구글_idToken = "googleToken";
	private String availAccessToken ;
	private String expiredAccessToken ;


	@Autowired
	private TokenVerifier tokenVerifier;

	@Test
	@DisplayName("가입한 유저 정보가 없을 때  idToken을 통해 로그인을 시도하면 기대하는 응답(Guest without token)을 반환한다.")
	void login_With_IdToken_when_before_join() throws Exception {

		//when
		var 응답 = 소셜_idToken으로_로그인_요청(애플, 회원가입안된_애플_idToken);

		//then
		로그인_응답_검증_게스트(응답, 회원가입안된_애플_idToken);
	}

	@Test
	@DisplayName("가입한 유저 정보가 있을 때  idToken을 통해 로그인을 시도하면 기대하는 응답(with token)을 반환한다.")
	void login_With_IdToken_when_after_join() throws Exception {
		//when
		var 응답 = 소셜_idToken으로_로그인_요청(애플, 회원가입된_애플_idToken);

		//then
		로그인_응답_검증_회원(응답, 회원가입된_애플_idToken);
	}

	@Test
	@DisplayName("가입한 유저 정보와 다른 ProviderType으로 로그인을 호출하면 기대하는 예외를 던진다.")
	void loginWithSocialIdToken_when_after_join_and_with_another_provider() throws Exception {
		//when
		var 응답 = 소셜_idToken으로_로그인_요청(애플, 회원가입된_구글_idToken);

		//then
		로그인_실패_검증(응답);

	}


	private void 로그인_실패_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(응답.body().asString()).contains("소셜 provider 불일치,");
	}

	private void 로그인_응답_검증_회원(ExtractableResponse<Response> 응답, String 검증할_idToken ) {

		OAuth2UserInfo info = tokenVerifier.verifyIdToken(검증할_idToken);
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());

		JsonPath response = 응답.body().jsonPath();
		assertThat(response.getString("guestUserId")).isNull();
		assertThat(response.getString("email")).isEqualTo(info.email());
		assertThat(response.getString("name")).isEqualTo(info.name());
		assertThat(response.getString("providerType")).isEqualTo(info.providerType().name());
		assertThat(response.getString("accessToken")).isNotNull();
		assertThat(response.getString("refreshToken")).isNotNull();
		assertThat(response.getString("jobFieldDetail")).isNotNull();
	}

	private void 로그인_응답_검증_게스트(ExtractableResponse<Response> 응답, String 검증할_idToken ) {

		OAuth2UserInfo info = tokenVerifier.verifyIdToken(검증할_idToken);
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());

		JsonPath response = 응답.body().jsonPath();
		assertThat(response.getString("guestUserId")).isNotNull();
		assertThat(response.getString("email")).isEqualTo(info.email());
		assertThat(response.getString("name")).isEqualTo(info.name());
		assertThat(response.getString("providerType")).isEqualTo(info.providerType().name());
		assertThat(response.getString("accessToken")).isNull();
		assertThat(response.getString("refreshToken")).isNull();
		assertThat(response.getString("jobFieldDetail")).isNull();
	}
}
