package kr.jay.okrver3.interfaces.team;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;

import kr.jay.okrver3.TestConfig;
import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.RoleType;
import kr.jay.okrver3.domain.user.User;

@Import(TestConfig.class)
@Transactional
@SpringBootTest
class TeamMemberApiControllerTest {

	@Autowired
	private TeamMemberApiController sut;

	@Test
	@DisplayName("팀원 추가를 시도하면 기대하는 응답(추가된 email주소)을 반환한다.")
	void invite_team_member() throws Exception {

		User user = new User(1L, "appleId", "appleUser", "apple@apple.com", "appleProfileImage", ProviderType.APPLE,
			RoleType.ADMIN, "pass");

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		final ResponseEntity<String> response = sut.inviteTeamMember(
			new TeamMemberInviteRequestDto("project-fgFHxGWeIUQt", "test@gmail.com"), auth);

		assertThat(response.getBody()).isEqualTo("test@gmail.com");
	}

}