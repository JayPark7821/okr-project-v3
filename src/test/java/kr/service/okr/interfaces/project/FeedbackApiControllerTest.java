package kr.service.okr.interfaces.project;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.List;
import java.util.regex.Pattern;

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
import kr.service.okr.interfaces.project.request.FeedbackSaveRequest;
import kr.service.okr.interfaces.project.response.FeedbackDetailResponse;
import kr.service.okr.interfaces.project.response.IniFeedbackResponse;
import kr.service.okr.interfaces.project.response.ProjectInitiativeResponse;
import kr.service.okr.util.SpringBootTestReady;

class FeedbackApiControllerTest extends SpringBootTestReady {

	@Autowired
	private FeedbackApiController sut;

	@PersistenceContext
	EntityManager em;

	@BeforeEach
	void beforeEach() {
		super.setUp();
		dataLoader.loadData(List.of("/insert-project-data.sql"));
	}

	@Test
	void 팀원의_행동전략에_피드백을_추가하면_기대하는_응답을_리턴한다() throws Exception {

		FeedbackSaveRequest requestDto =
			new FeedbackSaveRequest("피드백 작성", "GOOD_IDEA", "ini_ixYjj5aaafeab3AH8");

		ResponseEntity<String> response =
			sut.registerFeedback(
				requestDto,
				getAuthenticationToken(3L)
			);

		assertThat(response.getBody()).containsPattern(
			Pattern.compile("feedback-[a-zA-Z0-9]{11}"));
	}

	@Test
	void 행동전략토큰으로_getInitiativeFeedbacksBy를_호출하면_기대하는_응답IniFeedbackResponse를_리턴한다() throws Exception {
		String initiativeToken = "ini_ixYjj5nODqtb3AH8";

		IniFeedbackResponse response =
			sut.getInitiativeFeedbacksBy(
				initiativeToken,
				getAuthenticationToken(3L)
			).getBody();

		assertThat(response.myInitiative()).isTrue();
		assertThat(response.wroteFeedback()).isFalse();
		assertThat(response.feedback().size()).isEqualTo(2);
		assertThat(response.feedback().get(0).feedbackToken()).isEqualTo("feedback_el6q34zazzSyWx9");
		assertThat(response.feedback().get(1).feedbackToken()).isEqualTo("feedback_aaaaaagawe3rfwa3");
	}

	@Test
	void getCountOfInitiativeToGiveFeedback을_호출하면_아직_피드백을_남기지않은_팀원의_완료된_행동전략count를_리턴한다() throws Exception {

		Integer response = sut.getCountOfInitiativeToGiveFeedback(
			getAuthenticationToken(3L)
		).getBody();

		assertThat(response).isEqualTo(1);
	}

	@Test
	void getRecievedFeedback을_호출하면_기대한는_응답page_FeedbackDetailResponse를_리턴한다() throws Exception {
		List<String> feedbackTokenList = List.of("feedback_aaaaaagawe3rfwa3", "feedback_el6q34zazzSyWx9");
		String searchRange = "ALL";
		Page<FeedbackDetailResponse> response = sut.getRecievedFeedback(
			searchRange,
			getAuthenticationToken(3L),
			PageRequest.of(0, 5)
		).getBody();

		assertThat(response.getTotalElements()).isEqualTo(2);
		List<FeedbackDetailResponse> content = response.getContent();

		for (int i = 0; i < content.size(); i++) {
			FeedbackDetailResponse r = content.get(i);
			assertThat(r.feedbackToken()).isEqualTo(feedbackTokenList.get(i));
		}
	}

	@Test
	void getRequiredFeedbackInitiative을_호출하면_기대한는_응답_ProjectInitiativeResponse를_리턴한다() throws Exception {
		final List<ProjectInitiativeResponse> response =
			sut.getRequiredFeedbackInitiative(getAuthenticationToken(3L)).getBody();

		assertThat(response.size()).isEqualTo(1);
		assertThat(response.get(0).initiativeToken()).isEqualTo("ini_ixYjj5aaafeab3AH8");
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