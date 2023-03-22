package kr.service.okr.interfaces.notification;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import kr.service.okr.domain.user.User;
import kr.service.okr.interfaces.notification.response.NotificationResponse;
import kr.service.okr.util.SpringBootTestReady;

public class NotificationApiControllerTest extends SpringBootTestReady {

	@Autowired
	private NotificationApiController sut;

	@PersistenceContext
	EntityManager em;

	@BeforeEach
	void beforeEach() {
		super.setUp();
		dataLoader.loadData(List.of("/insert-project-data.sql"));
	}

	@Test
	void getNotifications을_호출화면_기대하는_응답을_리턴한다() throws Exception {
		List<String> notificationTokens = List.of("noti_aaaaaMoZey1SERx", "noti_e144441Zey1SERx");

		final ResponseEntity<Page<NotificationResponse>> response = sut.getNotifications(
			getAuthenticationToken(16L), PageRequest.of(0, 5)
		);

		assertThat(response.getBody().getTotalElements()).isEqualTo(2);
		List<NotificationResponse> content = response.getBody().getContent();

		for (int i = 0; i < content.size(); i++) {
			NotificationResponse r = content.get(i);
			assertThat(r.notiToken()).isEqualTo(notificationTokens.get(i));
		}
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

