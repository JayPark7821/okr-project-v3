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


	private UsernamePasswordAuthenticationToken getAuthenticationToken(long value) {
		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", value)
			.getSingleResult();

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());
		return auth;
	}
}