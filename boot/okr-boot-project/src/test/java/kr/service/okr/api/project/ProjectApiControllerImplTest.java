package kr.service.okr.api.project;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.service.okr.AuthenticationInfo;
import kr.service.okr.project.api.ProjectInfoResponse;
import kr.service.okr.project.api.RegisterProjectRequestDto;
import kr.service.okr.project.domain.enums.ProjectRoleType;
import kr.service.okr.project.persistence.entity.project.team.TeamMemberJpaEntity;
import kr.service.okr.user.persistence.entity.user.UserJpaEntity;
import kr.service.okr.utils.SpringBootTestReady;

class ProjectApiControllerImplTest extends SpringBootTestReady {

	@Autowired
	private ProjectApiControllerImpl sut;

	@BeforeEach
	void beforeEach() {
		super.setUp();
		dataLoader.loadData(List.of("/project-test-data.sql"));
	}

	@PersistenceContext
	EntityManager em;

	@Test
	@DisplayName("팀원 없이 프로젝트를 생성하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project() throws Exception {

		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		final ResponseEntity<String> response = sut.registerProject(
			new RegisterProjectRequestDto("projectObjective", projectSdt, projectEdt, List.of()),
			getAuthenticationInfo(112L)
		);

		assertThat(response.getBody()).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));
	}

	@Test
	@DisplayName("프로젝트를 생성시 팀원을 같이 입력하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project_with_team_members() throws Exception {
		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		final ResponseEntity<String> response = sut.registerProject(
			new RegisterProjectRequestDto("projectObjective", projectSdt, projectEdt, List.of("guest@email.com")),
			getAuthenticationInfo(112L)
		);

		assertThat(response.getBody()).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));

		final List<TeamMemberJpaEntity> teamMembers = em.createQuery(
				"select t from TeamMemberJpaEntity t where t.project.projectToken = :projectToken",
				TeamMemberJpaEntity.class)
			.setParameter("projectToken", response.getBody())
			.getResultList();

		assertThat(teamMembers.size()).isEqualTo(2);
	}

	@Test
	@DisplayName("projectToken으로 조회하면 기대하는 응답(ProjectResponse)을 반환한다.")
	void retrieve_project_with_project_token() throws Exception {

		ResponseEntity<ProjectInfoResponse> response =
			sut.getProjectInfoBy(
				"mst_Kiwqnp1Nq6lbTNn0",
				getAuthenticationInfo(112L)
			);

		assertThat(response.getBody().projectToken()).isEqualTo("mst_Kiwqnp1Nq6lbTNn0");
		assertThat(response.getBody().objective()).isEqualTo("팀 맴버 테스트용 프로젝트");
		assertThat(response.getBody().startDate()).isEqualTo("2022-12-07");
		assertThat(response.getBody().endDate()).isEqualTo("3999-12-14");
		assertThat(response.getBody().projectType()).isEqualTo("TEAM");
		assertThat(response.getBody().teamMembersCount()).isEqualTo(3);
		assertThat(response.getBody().roleType()).isEqualTo(ProjectRoleType.LEADER.name());
		assertThat(response.getBody().keyResults().size()).isEqualTo(3);
	}

	private AuthenticationInfo getAuthenticationInfo(Long userSeq) {
		final UserJpaEntity user = em.createQuery("select u from UserJpaEntity u where u.userSeq = :userSeq",
				UserJpaEntity.class)
			.setParameter("userSeq", userSeq)
			.getSingleResult();
		return new AuthenticationInfo(user.getUserSeq(), user.getEmail(), user.getUsername());
	}

}