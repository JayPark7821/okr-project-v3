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
import kr.service.okr.common.security.core.context.AuthenticationInfo;
import kr.service.okr.project.api.RegisterProjectRequestDto;
import kr.service.okr.project.persistence.entity.project.team.TeamMemberJpaEntity;
import kr.service.okr.user.enums.ProviderType;
import kr.service.okr.user.user.domain.JobField;
import kr.service.okr.user.user.domain.User;
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
			new AuthenticationInfo(
				User.builder()
					.userId("testId2")
					.userSeq(2L)
					.username("testUser2")
					.email("projectMasterTest@naver.com")
					.providerType(ProviderType.GOOGLE)
					.jobField(JobField.PRODUCER_CP)
					.profileImage("profile_image_url")
					.build()
			)
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
			new AuthenticationInfo(
				User.builder()
					.userId("testId2")
					.userSeq(2L)
					.username("testUser2")
					.email("projectMasterTest@naver.com")
					.providerType(ProviderType.GOOGLE)
					.jobField(JobField.PRODUCER_CP)
					.profileImage("profile_image_url")
					.build()
			)
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
}