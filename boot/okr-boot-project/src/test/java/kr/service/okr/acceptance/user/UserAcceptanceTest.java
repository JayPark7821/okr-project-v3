package kr.service.okr.acceptance.user;

import static kr.service.okr.acceptance.user.UserAcceptanceTestAssertions.*;
import static kr.service.okr.acceptance.user.UserAcceptanceTestSteps.*;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.service.okr.utils.SpringBootTestReady;

@DisplayName("User 도메인 인수 테스트")
public class UserAcceptanceTest extends SpringBootTestReady {

	private static final String 애플 = "APPLE";
	private static final String 회원가입안된_애플_idToken = "notMemberIdToken";

	String 사용자1_토큰;

	@Autowired
	DataSource dataSource;

	@BeforeEach
	void beforeEach() {
		super.setUp();

	}

	@Test
	@DisplayName("가입한 유저 정보가 없을 때  idToken을 통해 로그인을 시도하면 기대하는 응답(Guest without token)을 반환한다.")
	void login_With_IdToken_when_before_join() throws Exception {

		//when
		var 응답 = 소셜_idToken으로_로그인_요청(애플, 회원가입안된_애플_idToken);

		//then
		로그인_응답_검증_게스트(응답);
	}

}
