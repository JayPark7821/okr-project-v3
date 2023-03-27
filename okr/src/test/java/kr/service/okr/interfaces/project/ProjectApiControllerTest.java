package kr.service.okr.interfaces.project;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.domain.project.ProjectType;
import kr.service.okr.domain.project.aggregate.team.ProjectRoleType;
import kr.service.okr.domain.user.User;
import kr.service.okr.interfaces.project.request.ProjectSaveRequest;
import kr.service.okr.interfaces.project.response.ParticipateProjectResponse;
import kr.service.okr.interfaces.project.response.ProjectDetailResponse;
import kr.service.okr.interfaces.project.response.ProjectInfoResponse;
import kr.service.okr.interfaces.project.response.ProjectSideMenuResponse;
import kr.service.okr.util.SpringBootTestReady;

@Transactional
class ProjectApiControllerTest extends SpringBootTestReady {

	@Autowired
	private ProjectApiController sut;

	@PersistenceContext
	EntityManager em;

	@BeforeEach
	void beforeEach() {
		super.setUp();
		dataLoader.loadData(List.of("/insert-project-data.sql"));
	}

	@Test
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
			new ProjectSaveRequest("projectObjective", projectSdt, projectEdt, List.of("guest@email.com")), auth);

		assertThat(response.getBody()).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));
	}

	@Test
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
			new ProjectSaveRequest("projectObjective", projectSdt, projectEdt, List.of("guest@email.com")), auth);

		assertThat(response.getBody()).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));
	}

	@Test
	@DisplayName("projectToken으로 조회하면 기대하는 응답(ProjectResponse)을 반환한다.")
	void retrieve_project_with_project_token() throws Exception {

		UsernamePasswordAuthenticationToken auth = getAuthenticationToken(2L);

		ResponseEntity<ProjectInfoResponse> response = sut.getProjectInfoBy("mst_Kiwqnp1Nq6lbTNn0", auth);

		assertThat(response.getBody().projectToken()).isEqualTo("mst_Kiwqnp1Nq6lbTNn0");
		assertThat(response.getBody().objective()).isEqualTo("팀 맴버 테스트용 프로젝트");
		assertThat(response.getBody().startDate()).isEqualTo("2022-12-07");
		assertThat(response.getBody().endDate()).isEqualTo("3999-12-14");
		assertThat(response.getBody().projectType()).isEqualTo("TEAM");
	}

	@Test
	void 메인_페이지_프로젝트_조회시_조건에_따라_기대하는_응답을_리턴한다_최근생성순_종료된프로젝트_미포함_팀프로젝트() throws Exception {

		List<String> recentlyCreatedSortProject =
			List.of("mst_K4g4tfdaergg6421", "mst_3gbyy554frgg6421", "mst_K4232g4g5rgg6421");
		UsernamePasswordAuthenticationToken auth = getAuthenticationToken(13L);

		ResponseEntity<Page<ProjectDetailResponse>> response = sut.getDetailProjectList("RECENTLY_CREATE", "N",
			"TEAM",
			auth,
			PageRequest.of(0, 5));

		assertThat(response.getBody().getTotalElements()).isEqualTo(3L);
		List<ProjectDetailResponse> content = response.getBody().getContent();

		for (int i = 0; i < content.size(); i++) {
			ProjectDetailResponse r = content.get(i);
			assertThat(r.projectType()).isEqualTo(ProjectType.TEAM.name());
			assertThat(r.progress()).isLessThan(100);
			assertThat(r.projectToken()).isEqualTo(recentlyCreatedSortProject.get(i));
		}

	}

	@Test
	void 프로젝트_사이드_메뉴_조회시_기대하는_응답을_리턴한다_progress_team_members() throws Exception {
		String projectToken = "mst_K4g4tfdaergg6421";
		UsernamePasswordAuthenticationToken auth = getAuthenticationToken(13L);

		ResponseEntity<ProjectSideMenuResponse> response = sut.getProjectSideMenuDetails(projectToken, auth);

		assertThat(response.getBody().progress()).isEqualTo("60.0");
		assertThat(response.getBody().teamMembers().size()).isEqualTo(3);

	}

	@Test
	void 회원가입_탈퇴전_참여중인_프로젝트_리스트를_요청하면_기대하는_응답을_리턴한다_ParticipateProjectResponse() throws Exception {
		UsernamePasswordAuthenticationToken auth = getAuthenticationToken(3L);

		final List<ParticipateProjectResponse> response = sut.getParticipateProjects(auth).getBody();

		assertThat(response.size()).isEqualTo(8);
		assertThat(
			response.stream()
				.filter(t -> t.roleType().equals(ProjectRoleType.LEADER))
				.toList()
				.size()
		).isEqualTo(4);
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