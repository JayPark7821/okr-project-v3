package kr.service.user.acceptance.user;

import static kr.service.user.acceptance.user.UserAcceptanceTestAssertions.*;
import static kr.service.user.acceptance.user.UserAcceptanceTestSteps.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.service.user.utils.SpringBootTestReady;

@DisplayName("User 도메인 인수 테스트")
public class UserAcceptanceTest extends SpringBootTestReady {

	@BeforeEach
	void beforeEach() {
		super.setUp();
	}

	@Test
	@DisplayName("가입한 유저 정보가 없을 때 idToken을 통해 로그인을 시도하면 기대하는 응답(Guest)을 반환한다.")
	void login_With_IdToken_when_before_join() throws Exception {
		//given
		var 소셜타입 = "GOOGLE";
		var 소셜토큰 = "googleToken";
		//when
		var 응답 = 로그인_요청(소셜타입, 소셜토큰);

		//then
		로그인_요청_응답_검증_게스트(응답);

	}

}
