package kr.service.okr.interfaces.project;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.List;
import java.util.regex.Pattern;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import kr.service.okr.domain.user.User;
import kr.service.okr.interfaces.project.request.ProjectInitiativeSaveRequest;
import kr.service.okr.interfaces.project.response.InitiativeDetailResponse;
import kr.service.okr.interfaces.project.response.InitiativeForCalendarResponse;
import kr.service.okr.interfaces.project.response.ProjectInitiativeResponse;
import kr.service.okr.util.SpringBootTestReady;
import kr.service.okr.util.TestHelpUtils;

class InitiativeApiControllerTest extends SpringBootTestReady {

	@Autowired
	private InitiativeApiController sut;

	@PersistenceContext
	EntityManager em;

	@BeforeEach
	void beforeEach() {
		super.setUp();
		dataLoader.loadData(List.of("/insert-project-data.sql"));
	}

	@Test
	void 행동전략_추가시_기대하는_응답을_리턴한다_initiativeToken() throws Exception {

		ProjectInitiativeSaveRequest requestDto = new ProjectInitiativeSaveRequest(
			"key_wV6MX15WQ3DTzQMs",
			"행동전략",
			TestHelpUtils.getDateString(10, "yyyy-MM-dd"),
			TestHelpUtils.getDateString(-10, "yyyy-MM-dd"),
			"행동전략 상세내용"
		);

		UsernamePasswordAuthenticationToken auth = getAuthenticationToken(3L);

		ResponseEntity<String> response = sut.registerInitiative(requestDto, auth);

		assertThat(response.getBody()).containsPattern(
			Pattern.compile("initiative-[a-zA-Z0-9]{9}"));
	}

	@Test
	void 행동전략_완료시_기대하는_응답을_리턴한다() throws Exception {
		String initiativeToken = "ini_ixYjj5nODfeab3AH8";

		ResponseEntity<String> response = sut.initiativeFinished(initiativeToken, getAuthenticationToken(11L));

		assertThat(response.getBody()).isEqualTo("ini_ixYjj5nODfeab3AH8");
	}

	@Test
	void 핵심결과토큰으로_행동전략_리스트_조회시_기대하는_응답을_리턴한다() throws Exception {
		String keyResultToken = "key_wV6f45vWQaaazQaa";
		List<String> savedInitiativeTokenRecentlyCreatedOrder = List.of("ini_ixYjj5nODfeab3AH8",
			"ini_ixYjj5aaafeab3AH8", "ini_ixYjjnnnafeab3AH8");

		ResponseEntity<Page<ProjectInitiativeResponse>> response =
			sut.getInitiativeByKeyResultToken(keyResultToken, getAuthenticationToken(11L), PageRequest.of(0, 5));

		assertThat(response.getBody().getTotalElements()).isEqualTo(3);
		List<ProjectInitiativeResponse> content = response.getBody().getContent();

		for (int i = 0; i < content.size(); i++) {
			assertThat(content.get(i).initiativeToken()).isEqualTo(savedInitiativeTokenRecentlyCreatedOrder.get(i));
		}

	}

	@Test
	void 행동전략토큰으로_getInitiativeBy호출시_기대하는_응답_InitiativeDetailResponse를_리턴한다() throws Exception {
		String initiativeToken = "ini_ixYjj5nODqtb3AH8";

		ResponseEntity<InitiativeDetailResponse> response =
			sut.getInitiativeBy(
				initiativeToken,
				getAuthenticationToken(3L)
			);

		assertThat(response.getBody().done()).isTrue();
		assertThat(response.getBody().initiativeToken()).isEqualTo(initiativeToken);
		assertThat(response.getBody().initiativeName()).isEqualTo("ini name");
		assertThat(response.getBody().initiativeDetail()).isEqualTo("initiative detail1");
		assertThat(response.getBody().myInitiative()).isTrue();
		assertThat(response.getBody().user().userEmail()).isEqualTo("user1@naver.com");
	}

	@Test
	void 날짜로_getInitiativeByDate를_호출하면_기대하는_응답InitiativeForCalendarResponse를_size1_리턴한다() throws Exception {
		String date = "20231201";

		List<InitiativeForCalendarResponse> response =
			sut.getInitiativeByDate(
				date,
				getAuthenticationToken(14L)
			).getBody();

		assertThat(response.size()).isEqualTo(1);
		assertThat(response.get(0).initiativeToken()).isEqualTo("ini_ixYjj5na3fdab3AH8");
		assertThat(response.get(0).initiativeName()).isEqualTo("ini name876");
		assertThat(response.get(0).startDate()).isEqualTo("2000-12-12");
		assertThat(response.get(0).endDate()).isEqualTo("2023-12-14");
	}

	@Test
	void 날짜로_getInitiativeByDate를_호출하면_기대하는_응답InitiativeForCalendarResponse를_size3_리턴한다() throws Exception {
		String date = "20221201";

		List<InitiativeForCalendarResponse> response =
			sut.getInitiativeByDate(
				date,
				getAuthenticationToken(15L)
			).getBody();

		assertThat(response.size()).isEqualTo(3);
	}

	@Test
	void 년월로_getInitiativeDates를_호출하면_기대하는_응답을_리턴한다() throws Exception {
		String yearmonth = "2023-12";

		List<String> response =
			sut.getInitiativeDatesBy(
				yearmonth,
				getAuthenticationToken(15L)
			).getBody();

		assertThat(response.size()).isEqualTo(14);
		assertThat(response.get(0)).isEqualTo("2023-12-01");
		assertThat(response.get(response.size() - 1)).isEqualTo("2023-12-14");

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