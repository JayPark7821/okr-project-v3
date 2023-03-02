package kr.jay.okrver3.application.initiative;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDate;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import kr.jay.okrver3.application.project.ProjectFacade;
import kr.jay.okrver3.domain.project.service.impl.ProjectServiceImpl;
import kr.jay.okrver3.domain.user.User;

@DataJpaTest
@Import({InitiativeFacade.class, ProjectServiceImpl.class})
class InitiativeFacadeTest {

	@Autowired
	private ProjectFacade sut;

	@PersistenceContext
	EntityManager em;

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 행동전략_추가시_기대하는_응답을_리턴한다_initiativeToken() throws Exception {

		InitiativeSaveCommand requestDto = new InitiativeSaveCommand(
			"key_wV6MX15WQ3DTzQMs",
			"행동전략",
			LocalDate.now().minusDays(10),
			LocalDate.now().plusDays(10),
			"행동전략 상세내용"
		);

		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", 3L)
			.getSingleResult();

		String response = sut.registerInitiative(requestDto, user);

		assertThat(response).containsPattern(
			Pattern.compile("initiative-[a-zA-Z0-9]{10}"));
	}
}