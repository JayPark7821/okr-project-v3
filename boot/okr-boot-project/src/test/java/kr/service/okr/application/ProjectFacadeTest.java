package kr.service.okr.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import kr.service.okr.application.project.ProjectFacade;

@DataJpaTest
@Import({ProjectFacade.class
})
class ProjectFacadeTest {

	@Autowired
	private ProjectFacade sut;
	//
	// @PersistenceContext
	// EntityManager em;
	//
	// private User getUser(Long seq) {
	// 	User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
	// 		.setParameter("userSeq", seq)
	// 		.getSingleResult();
	// 	return user;
	// }

	@Test
	// @Sql("classpath:insert-user.sql")
	@DisplayName("팀원 추가 없이 프로젝트를 생성하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project() throws Exception {
		//
		// User user = getUser(999L);
		//
		// String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		// String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		//
		// String projectToken = sut.registerProject(
		// 	new ProjectSaveCommand("projectObjective", projectSdt, projectEdt, null), user.getUserSeq());
		//
		// Project result =
		// 	em.createQuery("select n from Project n where n.objective =: objective", Project.class)
		// 		.setParameter("objective", "projectObjective")
		// 		.getSingleResult();
		//
		// assertThat(result.getTeamMember().size()).isEqualTo(1);
		// assertThat(projectToken).containsPattern(
		// 	Pattern.compile("project-[a-zA-Z0-9]{12}"));
	}
}