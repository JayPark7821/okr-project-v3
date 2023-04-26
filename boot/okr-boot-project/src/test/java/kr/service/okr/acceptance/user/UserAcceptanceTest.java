package kr.service.okr.acceptance.user;

import static kr.service.okr.acceptance.user.UserAcceptanceTestAssertions.*;
import static kr.service.okr.acceptance.user.UserAcceptanceTestSteps.*;

import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.service.okr.user.api.JoinRequest;
import kr.service.okr.user.enums.JobCategory;
import kr.service.okr.utils.SpringBootTestReady;

@DisplayName("User 도메인 인수 테스트")
public class UserAcceptanceTest extends SpringBootTestReady {

	private static final String 애플 = "APPLE";
	private static final String 회원가입안된_애플_idToken = "notMemberAppleIdToken";
	private static final String 회원가입된_애플_idToken = "memberAppleIdToken";
	private static final String 회원가입된_구글_idToken = "memberGoogleIdToken";
	String 사용자1_토큰;

	@Autowired
	DataSource dataSource;

	@BeforeEach
	void beforeEach() {
		super.setUp();
		dataLoader.loadData(List.of("/project-test-data.sql"));
	}

	@Test
	@DisplayName("가입한 유저 정보가 없을 때  idToken을 통해 로그인을 시도하면 기대하는 응답(Guest without token)을 반환한다.")
	void login_With_IdToken_when_before_join() throws Exception {
		//when
		var 응답 = 소셜_idToken으로_로그인_요청(애플, 회원가입안된_애플_idToken);

		//then
		로그인_응답_검증_게스트(응답);
	}

	@Test
	@DisplayName("가입한 유저 정보가 있을 때  idToken을 통해 로그인을 시도하면 기대하는 응답(with token)을 반환한다.")
	void login_With_IdToken_when_after_join() throws Exception {
		//when
		var 응답 = 소셜_idToken으로_로그인_요청(애플, 회원가입된_애플_idToken);

		//then
		로그인_응답_검증_회원(응답);
	}

	@Test
	@DisplayName("가입한 유저 정보와 다른 ProviderType으로 로그인을 호출하면 기대하는 예외를 던진다.")
	void loginWithSocialIdToken_when_after_join_and_with_another_provider() throws Exception {
		//when
		var 응답 = 소셜_idToken으로_로그인_요청(애플, 회원가입된_구글_idToken);

		//then
		로그인_실패_검증_구글_가입(응답);

	}

	@Test
	@DisplayName("게스트 정보가 있을 때 join()을 호출하면 기대하는 응답을 반환한다.")
	void join_after_guest_login() throws Exception {
		//given
		var 회원가입_정보 =
			회원가입_정보_생성(
				"guest-ttdxe",
				"guest@email",
				"게스트",
				"WEB_SERVER_DEVELOPER"
			);

		//when
		var 응답 = 회원가입_요청(회원가입_정보);

		//then
		회원가입_요청_성공_검증(응답);
	}

	@Test
	@DisplayName("게스트 정보가 없을 때 join()을 호출하면 기대하는 예외를 던진다.")
	void join_before_guest_login() {
		//given
		var 회원가입_정보 =
			회원가입_정보_생성(
				"존재하지 않는 게스트_id",
				"존재하지않는게스트email@email.com",
				"없는 게스트",
				"WEB_SERVER_DEVELOPER"
			);

		//when
		var 응답 = 회원가입_요청(회원가입_정보);

		//then
		게스트_정보_없을_때_회원가입_요청_실패_검증(응답);
	}

	@Test
	@DisplayName("가입한 유저 정보가 있을 때 가입한 유저 정보로 join()을 호출하면 기대하는 예외를 던진다.")
	void join_again_when_after_join() throws Exception {
		//given

		var 회원가입_정보 =
			회원가입_정보_생성(
				"testId1",
				"teamMemberTest@naver.com",
				"testUser1",
				"PRODUCER_CP"
			);

		//when
		var 응답 = 회원가입_요청(회원가입_정보);

		//then
		이미_가입한_유저_회원가입_요청_실패_검증(응답);
	}

	@Test
	@DisplayName("회원 가입시 직업 카테고리 리스트를 조회 한다.")
	void get_jobCategory_list() throws Exception {
		//when
		var 응답 = 직업_카테고리_조회_요청();

		//then
		직업_카테고리_목록_응답_검증(응답);
	}

	@Test
	@DisplayName("회원 가입시 직업 카테고리로 직업 리스트를 조회 한다.")
	void get_jobField_list_by_jobCategory() throws Exception {
		//given
		var 백엔드_카테고리 = JobCategory.BACK_END;

		//when
		var 응답 = 직업_조회_요청(백엔드_카테고리);

		//then
		직업_목록_응답_검증(응답, 백엔드_카테고리);
	}

	private JoinRequest 회원가입_정보_생성(String 게스트_id, String 게스트_email, String 사용자명, String 직무_포지션) {
		return new JoinRequest(게스트_id, 사용자명, 게스트_email, 직무_포지션);
	}
}
