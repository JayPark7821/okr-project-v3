package kr.jay.okrver3.interfaces.project;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.domain.user.JobFieldDetail;
import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.RoleType;
import kr.jay.okrver3.domain.user.User;

@Transactional
@SpringBootTest
class ProjectApiControllerTest {

	@Autowired
	private ProjectApiController sut;

	@Test
	@DisplayName("프로젝트를 생성하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project() throws Exception {

		User user = new User(1L, "appleId", "appleUser", "apple@apple.com", "appleProfileImage", ProviderType.APPLE,
			RoleType.ADMIN, "pass", JobFieldDetail.WEB_FRONT_END_DEVELOPER);
		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		final ResponseEntity<String> response = sut.registerProject(
			new ProjectMasterSaveDto("projectName", projectSdt, projectEdt, "projectObjective",
				List.of("keyResult1", "keyResult2"), null), auth);

		assertThat(response.getBody()).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));
	}

	@Test
	@DisplayName("프로젝트를 생성시 팀원을 같이 입력하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project_with_team_members() throws Exception {

		User user = new User(1L, "appleId", "appleUser", "apple@apple.com", "appleProfileImage", ProviderType.APPLE,
			RoleType.ADMIN, "pass", JobFieldDetail.WEB_FRONT_END_DEVELOPER);
		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		final ResponseEntity<String> response = sut.registerProject(
			new ProjectMasterSaveDto("projectName", projectSdt, projectEdt, "projectObjective",
				List.of("keyResult1", "keyResult2"), List.of("guest@email.com")), auth);

		assertThat(response.getBody()).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("projectToken으로 조회하면 기대하는 응답(ProjectResponse)을 반환한다.")
	void retrieve_project_with_project_token() throws Exception {

		User user = new User(1L, "appleId", "appleUser", "apple@apple.com", "appleProfileImage", ProviderType.APPLE,
			RoleType.ADMIN, "pass", JobFieldDetail.WEB_FRONT_END_DEVELOPER);

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		ResponseEntity<ProjectInfoResponse> response = sut.getProjectInfoBy("project-fgFHxGWeIUQt", auth);

		assertThat(response.getBody().projectToken()).isEqualTo("project-fgFHxGWeIUQt");
		assertThat(response.getBody().name()).isEqualTo("projectName");
		assertThat(response.getBody().objective()).isEqualTo("projectObjective");
		assertThat(response.getBody().startDate()).isEqualTo("2020-12-01");
		assertThat(response.getBody().endDate()).isEqualTo("2020-12-12");
		assertThat(response.getBody().projectType()).isEqualTo("SINGLE");
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("팀원 추가를 시도하면 기대하는 응답(추가된 email주소)을 반환한다.")
	void invite_team_member() throws Exception {

		User user = new User(1L, "appleId", "appleUser", "apple@apple.com", "appleProfileImage", ProviderType.APPLE,
			RoleType.ADMIN, "pass", JobFieldDetail.WEB_FRONT_END_DEVELOPER);

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		final ResponseEntity<String> response = sut.inviteTeamMember(
			new TeamMemberInviteRequestDto("project-fgFHxGWeIUQt", "fakeAppleEmail"), auth);

		assertThat(response.getBody()).isEqualTo("fakeAppleEmail");
	}


	@Test
	@DisplayName("팀원 추가를 위해 email을 입력하면 기대하는 응답(email)을 반환한다.")
	void validate_email_address() throws Exception {

		String memberEmail = "guest@email.com";
		User user = new User(1L, "appleId", "appleUser", "apple@apple.com", "appleProfileImage", ProviderType.APPLE,
			RoleType.ADMIN, "pass", JobFieldDetail.WEB_FRONT_END_DEVELOPER);


		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		final ResponseEntity<String> response = sut.validateEmail("project-fgFHxGWeIUQt", memberEmail, auth );

		assertThat(response.getBody()).isEqualTo(memberEmail);
	}


	@Test
	@DisplayName("로그인한 유저가 속하지 않은 프로젝트에 팀원 추가를 위해 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_with_not_participating_project_throw_exception() throws Exception {

		String memberEmail = "guest@email.com";
		User user = new User(2L, "fakeAppleId", "fakeAppleName", "fakeAppleEmail", "fakeApplePic", ProviderType.APPLE,
			RoleType.ADMIN, "pass", JobFieldDetail.WEB_FRONT_END_DEVELOPER);


		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		final ResponseEntity<String> response = sut.validateEmail("project-fgFHxGWeIUQt", memberEmail, auth );

		assertThat(response.getBody()).isEqualTo(ErrorCode.INVALID_PROJECT_TOKEN.getMessage());
	}



	@Test
	@DisplayName("리더가 아닌 팀원이 팀원 추가를 위해 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void when_member_validate_email_address_will_throw_exception() throws Exception {
		String memberEmail = "guest@email.com";
		User user = new User(3L, "fakeGoogleId", "fakeGoogleName", "fakeGoogleIdEmail", "fakeGoogleIdPic", ProviderType.GOOGLE,
			RoleType.ADMIN, "pass", JobFieldDetail.WEB_SERVER_DEVELOPER);


		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		final ResponseEntity<String> response = sut.validateEmail("project-fgFHxGWeIUQt", memberEmail, auth );

		assertThat(response.getBody()).isEqualTo(ErrorCode.USER_IS_NOT_LEADER.getMessage());

	}

	@Test
	@DisplayName("팀원 추가를 위해 잘못된 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_exception() throws Exception {

		String wrongEmailAdd = "wrongEmailAdd";
		User user = new User(1L, "appleId", "appleUser", "apple@apple.com", "appleProfileImage", ProviderType.APPLE,
			RoleType.ADMIN, "pass", JobFieldDetail.WEB_FRONT_END_DEVELOPER);


		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		final ResponseEntity<String> response = sut.validateEmail("project-fgFHxGWeIUQt", wrongEmailAdd, auth );

		assertThat(response.getBody()).isEqualTo(ErrorCode.INVALID_USER_EMAIL.getMessage());
	}

	@Test
	@DisplayName("이미 팀에 초대된 팀원의 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_already_team_member() throws Exception {
		String teamMemberEmail = "fakeGoogleIdEmail";
		User user = new User(1L, "appleId", "appleUser", "apple@apple.com", "appleProfileImage", ProviderType.APPLE,
			RoleType.ADMIN, "pass", JobFieldDetail.WEB_FRONT_END_DEVELOPER);


		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		final ResponseEntity<String> response = sut.validateEmail("project-fgFHxGWeIUQt", teamMemberEmail, auth );

		assertThat(response.getBody()).isEqualTo(ErrorCode.USER_ALREADY_PROJECT_MEMBER.getMessage());
	}

	@Test
	@DisplayName("로그인된 유저 자신의 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_login_user_email() throws Exception {
		String userEmail = "'apple@apple.com'";
		User user = new User(1L, "appleId", "appleUser", "apple@apple.com", "appleProfileImage", ProviderType.APPLE,
			RoleType.ADMIN, "pass", JobFieldDetail.WEB_FRONT_END_DEVELOPER);

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		final ResponseEntity<String> response = sut.validateEmail("project-fgFHxGWeIUQt", userEmail, auth );

		assertThat(response.getBody()).isEqualTo(ErrorCode.NOT_AVAIL_INVITE_MYSELF.getMessage());
	}
}