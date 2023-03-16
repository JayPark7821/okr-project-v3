package kr.jay.okrver3.acceptance.user;

import static kr.jay.okrver3.acceptance.user.UserAcceptanceTestSteps.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.jay.okrver3.acceptance.AcceptanceTest;
import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.domain.user.JobCategory;
import kr.jay.okrver3.domain.user.JobField;
import kr.jay.okrver3.interfaces.user.request.JoinRequest;
import kr.jay.okrver3.interfaces.user.response.JobResponse;

@DisplayName("User domain acceptance test")
public class UserAcceptanceTest extends AcceptanceTest {

	private static final String 애플 = "APPLE";
	private static final String 회원가입안된_애플_idToken = "notMemberIdToken";
	private static final String 회원가입된_애플_idToken = "appleToken";
	private static final String 회원가입된_구글_idToken = "googleToken";


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
		로그인_실패_검증(응답);

	}

	@Test
	@DisplayName("게스트 정보가 있을 때 join()을 호출하면 기대하는 응답을 반환한다.")
	void join_after_guest_login() throws Exception{
		//given
		var 게스트_정보_응답 = 소셜_idToken으로_로그인_요청(애플, 회원가입안된_애플_idToken);

		var 회원가입_정보 =
			회원가입_정보_생성(
				응답에서_데이터_추출(게스트_정보_응답, "guestUserId"),
				응답에서_데이터_추출(게스트_정보_응답, "email"),
				"guest",
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
			회원가입_정보_생성("존재하지 않는 게스트_id", "존재하지않는게스트email@email.com","없는 게스트", "WEB_SERVER_DEVELOPER");

		//when
		var 응답 = 회원가입_요청(회원가입_정보);

		//then
		게스트_정보_없을_때_회원가입_요청_실패_검증(응답);
	}

	@Test
	@DisplayName("가입한 유저 정보가 있을 때 가입한 유저 정보로 join()을 호출하면 기대하는 예외를 던진다.")
	void join_again_when_after_join() throws Exception{
		//given
		var 게스트_정보_응답 = 소셜_idToken으로_로그인_요청(애플, 회원가입안된_애플_idToken);

		var 회원가입_정보 =
			회원가입_정보_생성(
				응답에서_데이터_추출(게스트_정보_응답, "guestUserId"),
				응답에서_데이터_추출(게스트_정보_응답, "email"),
				"guest",
				"WEB_SERVER_DEVELOPER"
			);

		회원가입_요청(회원가입_정보);

		//when
		var 응답 = 회원가입_요청(회원가입_정보);

		//then
		이미_가입한_유저_회원가입_요청_실패_검증(응답);
	}


	@Test
	@DisplayName("프로젝트 생성시 팀원을 추가하기 위해 email을 입력하면 기대하는 응답(email)을 반환한다.")
	void validate_email_address_for_register_project() throws Exception {
		//given
		String 가입된_유저_이메일 = "guest@email.com";

		//when
		var 응답 = 프로잭트_생성_전_email_검증_요청(가입된_유저_이메일, 로그인_유저);

		//then
		이메일_검증_응답_검증(응답, 가입된_유저_이메일);

	}

	@Test
	@DisplayName("회원 가입시 직업 카테고리 리스트를 조회 한다.")
	void get_jobCategory_list() throws Exception {
		//when
		var 응답 = 직업_카테고리_조회_요청();

		//then
		직업_카테고리_목록_검증(응답);

	}

	@Test
	@DisplayName("회원 가입시 직업 카테고리로 직업 리스트를 조회 한다.")
	void get_jobField_list_by_jobCategory() throws Exception {
		//given
		var 백엔드_카테고리 = JobCategory.BACK_END;

		//when
		var 응답 = 직업_조회_요청(백엔드_카테고리);

		//then
		직업_목록_검증(응답, 백엔드_카테고리);

	}

	@Test
	@DisplayName("선택된 직업으로 직업 카테고리를 요청하면 기대하는 응답을 반환한다")
	void get_jobCategory_by_jobField() throws Exception {
		//given
		var 직업_웹_서버_개발자 = JobField.WEB_SERVER_DEVELOPER;

		//when
		var 응답 = 직업_으로_직업_카테고리_조회_요청(직업_웹_서버_개발자);

		//then
		직업_카테고리_응답_검증(응답);

	}


	private void 직업_카테고리_응답_검증(ExtractableResponse<Response> 직업_목록) {
		assertThat(직업_목록.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(직업_목록.body().asString()).isEqualTo(JobCategory.BACK_END.getCode());
	}

	private void 직업_목록_검증(ExtractableResponse<Response> 직업_목록, JobCategory 카테고리) {
		assertThat(직업_목록.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(직업_목록.body().jsonPath().getList("", JobResponse.class))
			.isEqualTo(카테고리.getDetailList().stream().map(j->new JobResponse(j.getCode(),j.getTitle())).toList());
	}
	private void 직업_카테고리_목록_검증(ExtractableResponse<Response> 직업_카테고리_목록) {
		assertThat(직업_카테고리_목록.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(직업_카테고리_목록.body().jsonPath().getList("", JobResponse.class))
			.isEqualTo(Arrays.stream(JobCategory.values()).map(j->new JobResponse(j.getCode(),j.getTitle())).toList());
	}

	private void 이메일_검증_응답_검증(ExtractableResponse<Response> 응답, String 요청_이메일_주소) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(응답.body().asString()).isEqualTo(요청_이메일_주소);
	}
	private void 게스트_정보_없을_때_회원가입_요청_실패_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(응답.body().asString()).contains(ErrorCode.INVALID_JOIN_INFO.getMessage());
	}

	private void 이미_가입한_유저_회원가입_요청_실패_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(응답.body().asString()).contains(ErrorCode.ALREADY_JOINED_USER.getMessage());
	}

	private JoinRequest 회원가입_정보_생성(String 게스트_id, String 게스트_email, String 사용자명, String 직무_포지션) {
		return new JoinRequest(게스트_id, 사용자명, 게스트_email, 직무_포지션 );
	}

	private void 로그인_실패_검증(ExtractableResponse<Response> 응답) {
		assertThat(응답.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(응답.body().asString()).contains("소셜 provider 불일치,");
	}

	private void 회원가입_요청_성공_검증(ExtractableResponse<Response> 응답 ) {

		assertThat(응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

		JsonPath response = 응답.body().jsonPath();
		assertThat(response.getString("guestUserId")).isNull();
		assertThat(response.getString("email")).isNotNull();
		assertThat(response.getString("name")).isNotNull();
		assertThat(response.getString("providerType")).isNotNull();
		assertThat(response.getString("accessToken")).isNotNull();
		assertThat(response.getString("refreshToken")).isNotNull();
		assertThat(response.getString("jobFieldDetail")).isNotNull();
	}


	private void 로그인_응답_검증_회원(ExtractableResponse<Response> 응답 ) {

		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());

		JsonPath response = 응답.body().jsonPath();
		assertThat(response.getString("guestUserId")).isNull();
		assertThat(response.getString("email")).isNotNull();
		assertThat(response.getString("name")).isNotNull();
		assertThat(response.getString("providerType")).isNotNull();
		assertThat(response.getString("accessToken")).isNotNull();
		assertThat(response.getString("refreshToken")).isNotNull();
		assertThat(response.getString("jobFieldDetail")).isNotNull();
	}

	private void 로그인_응답_검증_게스트(ExtractableResponse<Response> 응답 ) {

		assertThat(응답.statusCode()).isEqualTo(HttpStatus.OK.value());

		JsonPath response = 응답.body().jsonPath();
		assertThat(response.getString("guestUserId")).isNotNull();
		assertThat(response.getString("email")).isNotNull();
		assertThat(response.getString("name")).isNotNull();
		assertThat(response.getString("providerType")).isNotNull();
		assertThat(response.getString("accessToken")).isNull();
		assertThat(response.getString("refreshToken")).isNull();
		assertThat(response.getString("jobFieldDetail")).isNull();
	}

	private String 응답에서_데이터_추출(ExtractableResponse<Response> 게스트_정보_응답, String field) {
		return 게스트_정보_응답.body().jsonPath().getString(field);
	}
}
