package kr.service.okr.interfaces.project;

import static kr.service.okr.util.TestHelpUtils.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.service.okr.interfaces.project.request.TeamMemberInviteRequest;
import kr.service.okr.util.SpringBootTestReady;
import kr.service.okrcommon.common.exception.ErrorCode;
import kr.service.okrcommon.common.exception.OkrApplicationException;

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

		final ResponseEntity<String> response = sut.inviteTeamMember(
			new TeamMemberInviteRequest("project-fgFHxGWeIUQt", "fakeAppleEmail"), getAuthenticationToken(em, 999L));

		assertThat(response.getBody()).isEqualTo("fakeAppleEmail");
	}

	@Test
	@DisplayName("팀원 추가를 위해 email을 입력하면 기대하는 응답(email)을 반환한다.")
	void validate_email_address() throws Exception {

		String memberEmail = "guest@email.com";

		final ResponseEntity<String> response = sut.validateEmailToInvite("project-fgFHxGWeIUQt", memberEmail,
			getAuthenticationToken(em, 999L));

		assertThat(response.getBody()).isEqualTo(memberEmail);
	}

	@Test
	@DisplayName("로그인한 유저가 속하지 않은 프로젝트에 팀원 추가를 위해 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_with_not_participating_project_throw_exception() throws Exception {

		String memberEmail = "guest@email.com";
		assertThatThrownBy(
			() -> sut.validateEmailToInvite("project-fgFHxGWeIUQt", memberEmail, getAuthenticationToken(em, 998L)))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.INVALID_PROJECT_TOKEN.getMessage());

	}

	@Test
	@DisplayName("리더가 아닌 팀원이 팀원 추가를 위해 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void when_member_validate_email_address_will_throw_exception() throws Exception {
		String memberEmail = "guest@email.com";

		assertThatThrownBy(
			() -> sut.validateEmailToInvite("project-fgFHxGWeIUQt", memberEmail, getAuthenticationToken(em, 997L)))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_IS_NOT_LEADER.getMessage());

	}

	@Test
	@DisplayName("팀원 추가를 위해 잘못된 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_exception() throws Exception {
		String wrongEmailAdd = "wrongEmailAdd";
		assertThatThrownBy(
			() -> sut.validateEmailToInvite("project-fgFHxGWeIUQt", wrongEmailAdd, getAuthenticationToken(em, 999L)))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.INVALID_USER_EMAIL.getMessage());
	}

	@Test
	@DisplayName("이미 팀에 초대된 팀원의 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_already_team_member() throws Exception {
		String teamMemberEmail = "fakeGoogleIdEmail";

		assertThatThrownBy(
			() -> sut.validateEmailToInvite("project-fgFHxGWeIUQt", teamMemberEmail, getAuthenticationToken(em, 999L)))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_ALREADY_PROJECT_MEMBER.getMessage());
	}

	@Test
	@DisplayName("로그인된 유저 자신의 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_login_user_email() throws Exception {
		String userEmail = "apple@apple.com";

		assertThatThrownBy(
			() -> sut.validateEmailToInvite("project-fgFHxGWeIUQt", userEmail, getAuthenticationToken(em, 999L)))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.NOT_AVAIL_INVITE_MYSELF.getMessage());
	}

}