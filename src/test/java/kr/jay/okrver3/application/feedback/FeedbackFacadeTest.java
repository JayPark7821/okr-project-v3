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



}