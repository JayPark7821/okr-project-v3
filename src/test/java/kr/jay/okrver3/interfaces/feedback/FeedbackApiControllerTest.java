package kr.jay.okrver3.interfaces.feedback;

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

import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.interfaces.feedback.request.FeedbackSaveRequest;

@Transactional
@SpringBootTest
class FeedbackApiControllerTest {

	@Autowired
	private FeedbackApiController sut;

	@PersistenceContext
	EntityManager em;

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 팀원의_행동전략에_피드백을_추가하면_기대하는_응답을_리턴한다() throws Exception {

		FeedbackSaveRequest requestDto =
			new FeedbackSaveRequest("피드백 작성", "GOOD_IDEA", "mst_Kiwqnp1Nq6lb6421",
				"ini_ixYjj5aaafeab3AH8");

		ResponseEntity<String> response =
			sut.registerFeedback(
				requestDto,
				getAuthenticationToken(3L)
			);

		assertThat(response.getBody()).containsPattern(
			Pattern.compile("initiative-[a-zA-Z0-9]{9}"));
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