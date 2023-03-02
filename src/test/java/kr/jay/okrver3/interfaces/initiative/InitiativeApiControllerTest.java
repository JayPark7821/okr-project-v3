package kr.jay.okrver3.interfaces.initiative;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import kr.jay.okrver3.TestHelpUtils;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.interfaces.project.ProjectInitiativeSaveDto;

@Transactional
@SpringBootTest
class InitiativeApiControllerTest {

	@Autowired
	private InitiativeApiController sut;

	@PersistenceContext
	EntityManager em;

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 행동전략_추가시_기대하는_응답을_리턴한다_initiativeToken() throws Exception {

		ProjectInitiativeSaveDto requestDto = new ProjectInitiativeSaveDto(
			"key_wV6MX15WQ3DTzQMs",
			"행동전략",
			TestHelpUtils.getDateString(10, "yyyy-MM-dd"),
			TestHelpUtils.getDateString(-100, "yyyy-MM-dd"),
			"행동전략 상세내용"
		);

		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", 3L)
			.getSingleResult();

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		ResponseEntity<String> response = sut.registerInitiative(requestDto, auth);

		assertThat(response.getBody()).containsPattern(
			Pattern.compile("initiative-[a-zA-Z0-9]{10}"));
	}

}