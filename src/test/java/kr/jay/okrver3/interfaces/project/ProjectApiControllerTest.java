package kr.jay.okrver3.interfaces.project;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.project.ProjectType;
import kr.jay.okrver3.domain.user.User;

@Transactional
@SpringBootTest
class ProjectApiControllerTest {

	@Autowired
	private ProjectApiController sut;

	@PersistenceContext
	EntityManager em;

	@Test
	@Sql("classpath:insert-user.sql")
	@DisplayName("프로젝트를 생성하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project() throws Exception {

		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", 1L)
			.getSingleResult();

		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		final ResponseEntity<String> response = sut.registerProject(
			new ProjectMasterSaveDto("projectObjective", projectSdt, projectEdt,
				List.of("keyResult1", "keyResult2"), null), auth);

		assertThat(response.getBody()).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));
	}

	@Test
	@Sql("classpath:insert-user.sql")
	@DisplayName("프로젝트를 생성시 팀원을 같이 입력하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project_with_team_members() throws Exception {

		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", 1L)
			.getSingleResult();

		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		final ResponseEntity<String> response = sut.registerProject(
			new ProjectMasterSaveDto("projectObjective", projectSdt, projectEdt,
				List.of("keyResult1", "keyResult2"), List.of("guest@email.com")), auth);

		assertThat(response.getBody()).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("projectToken으로 조회하면 기대하는 응답(ProjectResponse)을 반환한다.")
	void retrieve_project_with_project_token() throws Exception {

		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", 1L)
			.getSingleResult();

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		ResponseEntity<ProjectInfoResponse> response = sut.getProjectInfoBy("project-fgFHxGWeIUQt", auth);

		assertThat(response.getBody().projectToken()).isEqualTo("project-fgFHxGWeIUQt");
		assertThat(response.getBody().objective()).isEqualTo("projectObjective");
		assertThat(response.getBody().startDate()).isEqualTo("2020-12-01");
		assertThat(response.getBody().endDate()).isEqualTo("2020-12-12");
		assertThat(response.getBody().projectType()).isEqualTo("SINGLE");
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("팀원 추가를 시도하면 기대하는 응답(추가된 email주소)을 반환한다.")
	void invite_team_member() throws Exception {

		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", 1L)
			.getSingleResult();

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		final ResponseEntity<String> response = sut.inviteTeamMember(
			new TeamMemberInviteRequestDto("project-fgFHxGWeIUQt", "fakeAppleEmail"), auth);

		assertThat(response.getBody()).isEqualTo("fakeAppleEmail");
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("팀원 추가를 위해 email을 입력하면 기대하는 응답(email)을 반환한다.")
	void validate_email_address() throws Exception {

		String memberEmail = "guest@email.com";
		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", 1L)
			.getSingleResult();

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		final ResponseEntity<String> response = sut.validateEmail("project-fgFHxGWeIUQt", memberEmail, auth);

		assertThat(response.getBody()).isEqualTo(memberEmail);
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("로그인한 유저가 속하지 않은 프로젝트에 팀원 추가를 위해 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_with_not_participating_project_throw_exception() throws Exception {

		String memberEmail = "guest@email.com";
		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", 2L)
			.getSingleResult();

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		assertThatThrownBy(() -> sut.validateEmail("project-fgFHxGWeIUQt", memberEmail, auth))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.INVALID_PROJECT_TOKEN.getMessage());

	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("리더가 아닌 팀원이 팀원 추가를 위해 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void when_member_validate_email_address_will_throw_exception() throws Exception {
		String memberEmail = "guest@email.com";
		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", 3L)
			.getSingleResult();

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		assertThatThrownBy(() -> sut.validateEmail("project-fgFHxGWeIUQt", memberEmail, auth))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_IS_NOT_LEADER.getMessage());

	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("팀원 추가를 위해 잘못된 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_exception() throws Exception {

		String wrongEmailAdd = "wrongEmailAdd";
		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", 1L)
			.getSingleResult();

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		assertThatThrownBy(() -> sut.validateEmail("project-fgFHxGWeIUQt", wrongEmailAdd, auth))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.INVALID_USER_EMAIL.getMessage());
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("이미 팀에 초대된 팀원의 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_already_team_member() throws Exception {
		String teamMemberEmail = "fakeGoogleIdEmail";
		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", 1L)
			.getSingleResult();

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		assertThatThrownBy(() -> sut.validateEmail("project-fgFHxGWeIUQt", teamMemberEmail, auth))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_ALREADY_PROJECT_MEMBER.getMessage());
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("로그인된 유저 자신의 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_login_user_email() throws Exception {
		String userEmail = "apple@apple.com";
		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", 1L)
			.getSingleResult();

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		assertThatThrownBy(() -> sut.validateEmail("project-fgFHxGWeIUQt", userEmail, auth))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.NOT_AVAIL_INVITE_MYSELF.getMessage());
	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 메인_페이지_프로젝트_조회시_조건에_따라_기대하는_응답을_리턴한다_최근생성순_종료된프로젝트_미포함_팀프로젝트() throws Exception {

		List<String> recentlyCreatedSortProject = List.of("mst_3gbyy554frgg6421", "mst_K4232g4g5rgg6421");
		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", 13L)
			.getSingleResult();

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		ResponseEntity<Page<ProjectDetailResponse>> response = sut.getDetailProjectList("RECENTLY_CREATE", "N", "TEAM",
			auth,
			PageRequest.of(0, 5));

		assertThat(response.getBody().getTotalElements()).isEqualTo(2);
		List<ProjectDetailResponse> content = response.getBody().getContent();

		for (int i = 0; i < content.size(); i++) {
			ProjectDetailResponse r = content.get(i);
			assertThat(r.projectType()).isEqualTo(ProjectType.TEAM.name());
			assertThat(r.progress()).isLessThan(100);
			assertThat(r.projectToken()).isEqualTo(recentlyCreatedSortProject.get(i));
		}

	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 프로젝트_사이드_메뉴_조회시_기대하는_응답을_리턴한다_progress_team_members() throws Exception {
		String projectToken = "mst_K4g4tfdaergg6421";
		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", 13L)
			.getSingleResult();

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		ResponseEntity<ProjectSideMenuResponse> response = sut.getProjectSideMenuDetails(projectToken, auth);

		assertThat(response.getBody().progress()).isEqualTo("60.0");
		assertThat(response.getBody().teamMembers().size()).isEqualTo(3);

	}


	@Test
	@Sql("classpath:insert-project-date.sql")
	void 프로젝트_핵심결과_추가시_기대하는_응답을_리턴한다_keyResultToken() throws Exception {
		String projectToken = "mst_as3fg34tgg6421";
		String keyResultName = "keyResult";

		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", 13L)
			.getSingleResult();

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		ResponseEntity<String> response = sut.registerKeyResult(new ProjectKeyResultSaveDto(projectToken, keyResultName), auth);


		assertThat(response.getBody()).containsPattern(
			Pattern.compile("keyResult-[a-zA-Z0-9]{10}"));

	}

}