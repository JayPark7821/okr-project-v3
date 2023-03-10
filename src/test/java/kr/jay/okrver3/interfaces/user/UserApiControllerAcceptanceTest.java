package kr.jay.okrver3.interfaces.user;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.transaction.annotation.Transactional;

import com.google.api.client.json.Json;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import kr.jay.okrver3.TestConfig;
import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.utils.JwtTokenUtils;
import kr.jay.okrver3.infrastructure.user.auth.OAuth2UserInfo;
import kr.jay.okrver3.infrastructure.user.auth.TokenVerifier;
import kr.jay.okrver3.interfaces.project.response.IniFeedbackResponse;
import kr.jay.okrver3.interfaces.user.request.JoinRequest;
import kr.jay.okrver3.interfaces.user.request.UserInfoUpdateRequest;
import kr.jay.okrver3.interfaces.user.response.JobResponse;
import kr.jay.okrver3.interfaces.user.response.UserInfoResponse;

@Import(TestConfig.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserApiControllerAcceptanceTest {

	@LocalServerPort
	private int port;

	@Value("${app.auth.tokenSecret}")
	private String key;

	@Value("${app.auth.refreshTokenRegenerationThreshold}")
	private Long refreshTokenRegenerationThreshold;

	@Autowired
	DataSource dataSource;
	@Autowired
	private TokenVerifier tokenVerifier;
	private static final String PROVIDER_APPLE = "APPLE";
	private static final String NOT_MEMBER_APPLE_ID_TOKEN = "notMemberIdToken";
	private static final String APPLE_ID_TOKEN = "appleToken";
	private static final String GOOGLE_ID_TOKEN = "googleToken";
	private String availAccessToken ;
	private String expiredAccessToken ;
	private static final String baseUrl = "/api/v1/user";



	@BeforeAll
	void setUpAll() {

		try (Connection conn = dataSource.getConnection()) {

			availAccessToken = JwtTokenUtils.generateToken("apple@apple.com", key, refreshTokenRegenerationThreshold + 10000000L);
			expiredAccessToken = JwtTokenUtils.generateToken("fakeAppleEmail", key, refreshTokenRegenerationThreshold - 10000000L);
			String sql = "insert into refresh_token (refresh_token_seq, user_email, refresh_token) "
						+ "values ('9999', 'apple@apple.com', ?) ,"
								+ "('9998', 'fakeAppleEmail', ? )";

			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, availAccessToken);
			statement.setString(2, expiredAccessToken);
			statement.executeUpdate();

			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/insert-user.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/insert-project.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/insert-team.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/insert-keyresult.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/insert-initiative.sql"));
			ScriptUtils.executeSqlScript(conn, new ClassPathResource("/insert-guest.sql"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@Test
	@DisplayName("????????? ?????? ????????? ?????? ???  idToken??? ?????? ???????????? ???????????? ???????????? ??????(Guest without token)??? ????????????.")
	void login_With_IdToken_when_before_join() throws Exception {
		final JsonPath response = RestAssured.

			given()
			.contentType(ContentType.JSON).

			when()
			.post("/api/v1/user/login/" + PROVIDER_APPLE + "/" + NOT_MEMBER_APPLE_ID_TOKEN).

			then()
			.statusCode(HttpStatus.OK.value())
			.extract().jsonPath();

		assertResponseWithFixture(response,"notMemberIdToken");
	}

	@Test
	@DisplayName("????????? ?????? ????????? ?????? ???  idToken??? ?????? ???????????? ???????????? ???????????? ??????(with token)??? ????????????.")
	void login_With_IdToken_when_after_join() throws Exception {

		final JsonPath response = RestAssured.

			given()
			.contentType(ContentType.JSON).

			when()
			.post("/api/v1/user/login/" + PROVIDER_APPLE + "/" + APPLE_ID_TOKEN).

			then()
			.statusCode(HttpStatus.OK.value())
			.extract().jsonPath();

		assertLoginUser(response);

	}

	@Test
	@DisplayName("????????? ?????? ????????? ?????? ProviderType?????? ???????????? ???????????? ???????????? ????????? ?????????.")
	void loginWithSocialIdToken_when_after_join_and_with_another_provider() {
		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON).

			when()
			.post("/api/v1/user/login/" + PROVIDER_APPLE + "/" + GOOGLE_ID_TOKEN).

			then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.extract().body().asString();

		assertThat(response).isEqualTo("?????? provider ?????????, GOOGLE(???)??? ????????? ????????? ????????????.");
	}

	@Test
	@DisplayName("????????? ????????? ?????? ??? join()??? ???????????? ???????????? ????????? ????????????.")
	void join_after_guest_login() {

		final JsonPath response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.body(new JoinRequest("guest-rkmZUIUNWk3333", "guest", "guestJoinTest@email.com", "WEB_SERVER_DEVELOPER")).

			when()
			.post("/api/v1/user/join").

			then()
			.statusCode(HttpStatus.CREATED.value())
			.extract().jsonPath();

		assertLoginUser(response);

	}

	@Test
	@DisplayName("????????? ????????? ?????? ??? join()??? ???????????? ???????????? ????????? ?????????.")
	void join_before_guest_login() {
		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.body(new JoinRequest("not-registered-guest-id", "guest", "guest@email.com", "Developer")).

			when()
			.post("/api/v1/user/join").

			then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.extract().body().asString();

		assertThat(response).isEqualTo(ErrorCode.INVALID_JOIN_INFO.getMessage());
	}

	@Test
	// @Sql({"classpath:insert-user.sql", "classpath:insert-guest.sql"})
	@DisplayName("????????? ?????? ????????? ?????? ??? join()??? ???????????? ???????????? ????????? ?????????.")
	void join_again_when_after_join() {
		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.body(new JoinRequest("guest-rkmZUIUNWkSMX3", "appleUser", "guest@email.com", "WEB_SERVER_DEVELOPER")).

			when()
			.post("/api/v1/user/join").

			then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.extract().body().asString();

		assertThat(response).isEqualTo(ErrorCode.ALREADY_JOINED_USER.getMessage());
	}

	@Test
	void refreshToken??????_getNewAccessToken???_????????????_????????????_?????????_????????????_new_accessToken() {

		final JsonPath response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + availAccessToken).

			when()
			.get("/api/v1/user/refresh").

			then()
			.statusCode(HttpStatus.OK.value())
			.extract().jsonPath();

		assertThat(response.getString("accessToken")).isNotNull();
		assertThat(response.getString("refreshToken")).isEqualTo(availAccessToken);
	}

	@Test
	void refreshToken???_?????????_?????????_????????????_getNewAccessToken???_????????????_????????????_?????????_????????????_new_accessToken() {

		final JsonPath response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + expiredAccessToken).

			when()
			.get("/api/v1/user/refresh").

			then()
			.statusCode(HttpStatus.OK.value())
			.extract().jsonPath();

		assertThat(response.getString("accessToken")).isNotNull();
		assertThat(response.getString("refreshToken")).isNotEqualTo(expiredAccessToken);
	}

	@Test
	@DisplayName("???????????? ????????? ????????? ???????????? ?????? email??? ???????????? ???????????? ??????(email)??? ????????????.")
	void validate_email_address_for_register_project() throws Exception {
		String memberEmail = "guest@email.com";
		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + availAccessToken).

			when()
			.get(baseUrl + "/validate" + "/" + memberEmail).

			then()
			.statusCode(HttpStatus.OK.value())
			.extract().body().asString();

		assertThat(response).isEqualTo(memberEmail);
	}

	@Test
	void getJobCategory???_????????????_????????????_??????_JobResponse???_????????????() throws Exception {

		final JsonPath response = RestAssured.

			given()
			.contentType(ContentType.JSON).

			when()
			.get(baseUrl + "/job/category").

			then()
			.statusCode(HttpStatus.OK.value())
			.extract().body().jsonPath();

		assertThat(response.getList("",JobResponse.class).size()).isEqualTo(6);
	}

	@Test
	void getJobField???_????????????_????????????_??????_JobResponse???_????????????() throws Exception {

		String category = "BACK_END";
		final JsonPath response = RestAssured.

			given()
			.contentType(ContentType.JSON).

			when()
			.get(baseUrl + "/job/"+ category + "/fields").

			then()
			.statusCode(HttpStatus.OK.value())
			.extract().body().jsonPath();

		assertThat(response.getList("",JobResponse.class).size()).isEqualTo(4);
	}

	@Test
	void ????????????_jobCategory???_getJobField???_????????????_????????????_??????_exception_????????????() throws Exception {

		String category = "BACK_ENDDDD";
		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON).

			when()
			.get(baseUrl + "/job/"+ category + "/fields").

			then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.extract().body().asString();

		assertThat(response).isEqualTo(ErrorCode.INVALID_JOB_CATEGORY.getMessage());
	}

	@Test
	void getJobCategoryBy???_????????????_????????????_??????_JobCategory???_????????????() throws Exception {

		String jobField = "WEB_FRONT_END_DEVELOPER";
		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON).

			when()
			.get(baseUrl + "/job/field/"+ jobField).

			then()
			.statusCode(HttpStatus.OK.value())
			.extract().body().asString();

		assertThat(response).isEqualTo("FRONT_END");
	}


	@Test
	void ????????????_jobField???_getJobCategoryBy???_????????????_????????????_??????_exception_????????????() throws Exception {

		String jobField = "WEB_FRONT_END_DEVELOPERRRRR";
		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON).

			when()
			.get(baseUrl + "/job/field/"+ jobField).

			then()
			.statusCode(HttpStatus.BAD_REQUEST.value())
			.extract().body().asString();

		assertThat(response).isEqualTo(ErrorCode.INVALID_JOB_DETAIL_FIELD.getMessage());
	}

	@Test
	void getUserInfo???_????????????_????????????_??????_UserInfoResponse???_????????????() throws Exception {

		final UserInfoResponse response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + availAccessToken).


			when()
			.get(baseUrl).

			then()
			.statusCode(HttpStatus.OK.value())
			.extract().body().jsonPath()
			.getObject("", UserInfoResponse.class);

		assertThat(response.email()).isEqualTo("apple@apple.com");
		assertThat(response.name()).isEqualTo("appleUser");
	}

	@Test
	void updateUserInfo???_????????????_????????????_?????????_????????????() throws Exception {
		String newUserName = "newName";
		String newJobField = "LAW_LABOR";
		final String response = RestAssured.

			given()
			.contentType(ContentType.JSON)
			.header("Authorization", "Bearer " + availAccessToken)
			.body(new UserInfoUpdateRequest(newUserName,"profileImage", newJobField)).

			when()
			.put(baseUrl).

			then()
			.statusCode(HttpStatus.OK.value())
			.extract().body().asString();

		assertThat(response).isEqualTo("SUCCESS");
	}


	private void assertLoginUser(JsonPath response) {
		assertThat(response.getString("guestId")).isNull();
		assertThat(response.getString("email")).isNotNull();
		assertThat(response.getString("name")).isNotNull();
		assertThat(response.getString("providerType")).isNotNull();
		assertThat(response.getString("accessToken")).isNotNull();
		assertThat(response.getString("refreshToken")).isNotNull();
		assertThat(response.getString("jobFieldDetail")).isNotNull();
	}

	private void assertResponseWithFixture(JsonPath response, String fixture) {

		OAuth2UserInfo info = tokenVerifier.verifyIdToken(fixture);

		assertThat(response.getString("guestUserId")).isNotNull();
		assertThat(response.getString("email")).isEqualTo(info.email());
		assertThat(response.getString("name")).isEqualTo(info.name());
		assertThat(response.getString("providerType")).isEqualTo(info.providerType().name());
		assertThat(response.getString("accessToken")).isNull();
		assertThat(response.getString("refreshToken")).isNull();
		assertThat(response.getString("jobFieldDetail")).isNull();
	}

}
