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

import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.RoleType;
import kr.jay.okrver3.domain.user.User;

@SpringBootTest
class ProjectApiControllerTest {

	@Autowired
	private ProjectApiController sut;

	@Test
	@DisplayName("프로젝트를 생성하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project() throws Exception {

		User user = new User(1L, "appleId", "appleUser", "apple@apple.com", "appleProfileImage", ProviderType.APPLE,
			RoleType.ADMIN, "pass");
		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		final ResponseEntity<String> response = sut.registerProject(
			new ProjectMasterSaveDto("projectName", projectSdt, projectEdt, "projectObjective",
				List.of("keyResult1", "keyResult2")), auth);

		assertThat(response.getBody()).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("projectToken으로 조회하면 기대하는 응답(ProjectResponse)을 반환한다.")
	void retrieve_project_with_project_token() throws Exception {

		User user = new User(1L, "appleId", "appleUser", "apple@apple.com", "appleProfileImage", ProviderType.APPLE,
			RoleType.ADMIN, "pass");

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