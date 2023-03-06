package kr.jay.okrver3.application.feedback;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.interfaces.feedback.FeedbackSaveCommand;
import kr.jay.okrver3.interfaces.feedback.request.FeedbackSaveRequest;

@DataJpaTest
class FeedbackFacadeTest {

	@Autowired
	private FeedbackFacade sut;

	@PersistenceContext
	EntityManager em;

	private User getUser(Long seq) {
		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", seq)
			.getSingleResult();
		return user;
	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 팀원의_행동전략에_피드백을_추가하면_기대하는_응답을_리턴한다() throws Exception {

		FeedbackSaveCommand command =
			new FeedbackSaveCommand("피드백 작성", "GOOD_IDEA", "mst_Kiwqnp1Nq6lb6421",
				"ini_ixYjj5aaafeab3AH8");

		String response =
			sut.registerFeedback(
				command,
				getUser(3L)
			);

		assertThat(response).containsPattern(
			Pattern.compile("initiative-[a-zA-Z0-9]{9}"));
	}

}