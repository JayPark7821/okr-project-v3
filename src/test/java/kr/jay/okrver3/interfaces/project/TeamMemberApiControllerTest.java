package kr.jay.okrver3.interfaces.project;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.interfaces.project.request.TeamMemberInviteRequest;
import kr.jay.okrver3.util.SpringBootTestReady;

class TeamMemberApiControllerTest extends SpringBootTestReady {

	@Autowired
	private TeamMemberApiController sut;

	@PersistenceContext
	EntityManager em;

	@BeforeEach
	void beforeEach() {
		super.setUp();
		dataLoader.loadData(List.of("/insert-project.sql", "/insert-user.sql", "/insert-team.sql"));
	}

	@Test
	@DisplayName("팀원 추가를 시도하면 기대하는 응답(추가된 email주소)을 반환한다.")
	void invite_team_member() throws Exception {

		UsernamePasswordAuthenticationToken auth = getAuthenticationToken(999L);

		final ResponseEntity<String> response = sut.inviteTeamMember(
			new TeamMemberInviteRequest("project-fgFHxGWeIUQt", "fakeAppleEmail"), auth);

		assertThat(response.getBody()).isEqualTo("fakeAppleEmail");
	}

	@Test
	@DisplayName("팀원 추가를 위해 email을 입력하면 기대하는 응답(email)을 반환한다.")
	void validate_email_address() throws Exception {

		String memberEmail = "guest@email.com";
		UsernamePasswordAuthenticationToken auth = getAuthenticationToken(999L);

		final ResponseEntity<String> response = sut.validateEmailToInvite("project-fgFHxGWeIUQt", memberEmail, auth);

		assertThat(response.getBody()).isEqualTo(memberEmail);
	}

	@Test
	@DisplayName("로그인한 유저가 속하지 않은 프로젝트에 팀원 추가를 위해 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_with_not_participating_project_throw_exception() throws Exception {

		String memberEmail = "guest@email.com";
		UsernamePasswordAuthenticationToken auth = getAuthenticationToken(998L);

		assertThatThrownBy(() -> sut.validateEmailToInvite("project-fgFHxGWeIUQt", memberEmail, auth))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.INVALID_PROJECT_TOKEN.getMessage());

	}

	@Test
	@DisplayName("리더가 아닌 팀원이 팀원 추가를 위해 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void when_member_validate_email_address_will_throw_exception() throws Exception {
		String memberEmail = "guest@email.com";
		UsernamePasswordAuthenticationToken auth = getAuthenticationToken(997L);

		assertThatThrownBy(() -> sut.validateEmailToInvite("project-fgFHxGWeIUQt", memberEmail, auth))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_IS_NOT_LEADER.getMessage());

	}

	@Test
	@DisplayName("팀원 추가를 위해 잘못된 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_exception() throws Exception {

		String wrongEmailAdd = "wrongEmailAdd";
		UsernamePasswordAuthenticationToken auth = getAuthenticationToken(999L);

		assertThatThrownBy(() -> sut.validateEmailToInvite("project-fgFHxGWeIUQt", wrongEmailAdd, auth))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.INVALID_USER_EMAIL.getMessage());
	}

	@Test
	@DisplayName("이미 팀에 초대된 팀원의 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_already_team_member() throws Exception {
		String teamMemberEmail = "fakeGoogleIdEmail";
		UsernamePasswordAuthenticationToken auth = getAuthenticationToken(999L);

		assertThatThrownBy(() -> sut.validateEmailToInvite("project-fgFHxGWeIUQt", teamMemberEmail, auth))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_ALREADY_PROJECT_MEMBER.getMessage());
	}

	@Test
	@DisplayName("로그인된 유저 자신의 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_login_user_email() throws Exception {
		String userEmail = "apple@apple.com";
		UsernamePasswordAuthenticationToken auth = getAuthenticationToken(999L);

		assertThatThrownBy(() -> sut.validateEmailToInvite("project-fgFHxGWeIUQt", userEmail, auth))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.NOT_AVAIL_INVITE_MYSELF.getMessage());
	}

	private UsernamePasswordAuthenticationToken getAuthenticationToken(long value) {
		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", value)
			.getSingleResult();

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());
		return auth;
	}
}