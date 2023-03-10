package kr.jay.okrver3.interfaces.project;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import kr.jay.okrver3.TestHelpUtils;
import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.project.ProjectType;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.interfaces.project.request.FeedbackSaveRequest;
import kr.jay.okrver3.interfaces.project.request.ProjectInitiativeSaveRequest;
import kr.jay.okrver3.interfaces.project.request.ProjectKeyResultSaveRequest;
import kr.jay.okrver3.interfaces.project.request.ProjectSaveRequest;
import kr.jay.okrver3.interfaces.project.request.TeamMemberInviteRequest;
import kr.jay.okrver3.interfaces.project.response.IniFeedbackResponse;
import kr.jay.okrver3.interfaces.project.response.InitiativeDetailResponse;
import kr.jay.okrver3.interfaces.project.response.InitiativeForCalendarResponse;
import kr.jay.okrver3.interfaces.project.response.ProjectDetailResponse;
import kr.jay.okrver3.interfaces.project.response.ProjectInfoResponse;
import kr.jay.okrver3.interfaces.project.response.ProjectInitiativeResponse;
import kr.jay.okrver3.interfaces.project.response.ProjectSideMenuResponse;

@Transactional
@SpringBootTest
class ProjectApiControllerTest {

	@Autowired
	private ProjectApiController sut;

	@PersistenceContext
	EntityManager em;

	@Test
	@Sql("classpath:insert-user.sql")
	@DisplayName("프로젝트를 생성하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project() throws Exception {

		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", 999L)
			.getSingleResult();

		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		final ResponseEntity<String> response = sut.registerProject(
			new ProjectSaveRequest("projectObjective", projectSdt, projectEdt,
				List.of("keyResult1", "keyResult2"), List.of("guest@email.com")), auth);

		assertThat(response.getBody()).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));
	}

	@Test
	@Sql("classpath:insert-user.sql")
	@DisplayName("프로젝트를 생성시 팀원을 같이 입력하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project_with_team_members() throws Exception {

		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", 999L)
			.getSingleResult();

		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());

		final ResponseEntity<String> response = sut.registerProject(
			new ProjectSaveRequest("projectObjective", projectSdt, projectEdt,
				List.of("keyResult1", "keyResult2"), List.of("guest@email.com")), auth);

		assertThat(response.getBody()).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));
	}


	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("projectToken으로 조회하면 기대하는 응답(ProjectResponse)을 반환한다.")
	void retrieve_project_with_project_token() throws Exception {

		UsernamePasswordAuthenticationToken auth = getAuthenticationToken(999L);

		ResponseEntity<ProjectInfoResponse> response = sut.getProjectInfoBy("project-fgFHxGWeIUQt", auth);

		assertThat(response.getBody().projectToken()).isEqualTo("project-fgFHxGWeIUQt");
		assertThat(response.getBody().objective()).isEqualTo("projectObjective");
		assertThat(response.getBody().startDate()).isEqualTo("2020-12-01");
		assertThat(response.getBody().endDate()).isEqualTo("3999-12-12");
		assertThat(response.getBody().projectType()).isEqualTo("SINGLE");
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("팀원 추가를 시도하면 기대하는 응답(추가된 email주소)을 반환한다.")
	void invite_team_member() throws Exception {

		UsernamePasswordAuthenticationToken auth = getAuthenticationToken(999L);

		final ResponseEntity<String> response = sut.inviteTeamMember(
			new TeamMemberInviteRequest("project-fgFHxGWeIUQt", "fakeAppleEmail"), auth);

		assertThat(response.getBody()).isEqualTo("fakeAppleEmail");
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("팀원 추가를 위해 email을 입력하면 기대하는 응답(email)을 반환한다.")
	void validate_email_address() throws Exception {

		String memberEmail = "guest@email.com";
		UsernamePasswordAuthenticationToken auth = getAuthenticationToken(999L);

		final ResponseEntity<String> response = sut.validateEmailToInvite("project-fgFHxGWeIUQt", memberEmail, auth);

		assertThat(response.getBody()).isEqualTo(memberEmail);
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("로그인한 유저가 속하지 않은 프로젝트에 팀원 추가를 위해 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_with_not_participating_project_throw_exception() throws Exception {

		String memberEmail = "guest@email.com";
		UsernamePasswordAuthenticationToken auth = getAuthenticationToken(998L);

		assertThatThrownBy(() -> sut.validateEmailToInvite("project-fgFHxGWeIUQt", memberEmail, auth))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.INVALID_PROJECT_TOKEN.getMessage());

	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("리더가 아닌 팀원이 팀원 추가를 위해 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void when_member_validate_email_address_will_throw_exception() throws Exception {
		String memberEmail = "guest@email.com";
		UsernamePasswordAuthenticationToken auth = getAuthenticationToken(997L);

		assertThatThrownBy(() -> sut.validateEmailToInvite("project-fgFHxGWeIUQt", memberEmail, auth))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_IS_NOT_LEADER.getMessage());

	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("팀원 추가를 위해 잘못된 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_exception() throws Exception {

		String wrongEmailAdd = "wrongEmailAdd";
		UsernamePasswordAuthenticationToken auth = getAuthenticationToken(999L);

		assertThatThrownBy(() -> sut.validateEmailToInvite("project-fgFHxGWeIUQt", wrongEmailAdd, auth))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.INVALID_USER_EMAIL.getMessage());
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("이미 팀에 초대된 팀원의 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_already_team_member() throws Exception {
		String teamMemberEmail = "fakeGoogleIdEmail";
		UsernamePasswordAuthenticationToken auth = getAuthenticationToken(999L);

		assertThatThrownBy(() -> sut.validateEmailToInvite("project-fgFHxGWeIUQt", teamMemberEmail, auth))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_ALREADY_PROJECT_MEMBER.getMessage());
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("로그인된 유저 자신의 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_login_user_email() throws Exception {
		String userEmail = "apple@apple.com";
		UsernamePasswordAuthenticationToken auth = getAuthenticationToken(999L);

		assertThatThrownBy(() -> sut.validateEmailToInvite("project-fgFHxGWeIUQt", userEmail, auth))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.NOT_AVAIL_INVITE_MYSELF.getMessage());
	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 메인_페이지_프로젝트_조회시_조건에_따라_기대하는_응답을_리턴한다_최근생성순_종료된프로젝트_미포함_팀프로젝트() throws Exception {

		List<String> recentlyCreatedSortProject = List.of("mst_3gbyy554frgg6421", "mst_K4232g4g5rgg6421");
		UsernamePasswordAuthenticationToken auth = getAuthenticationToken(13L);

		ResponseEntity<Page<ProjectDetailResponse>> response = sut.getDetailProjectList("RECENTLY_CREATE", "N",
			"TEAM",
			auth,
			PageRequest.of(0, 5));

		assertThat(response.getBody().getTotalElements()).isEqualTo(2);
		List<ProjectDetailResponse> content = response.getBody().getContent();

		for (int i = 0; i < content.size(); i++) {
			ProjectDetailResponse r = content.get(i);
			assertThat(r.projectType()).isEqualTo(ProjectType.TEAM.name());
			assertThat(r.progress()).isLessThan(100);
			assertThat(r.projectToken()).isEqualTo(recentlyCreatedSortProject.get(i));
		}

	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 프로젝트_사이드_메뉴_조회시_기대하는_응답을_리턴한다_progress_team_members() throws Exception {
		String projectToken = "mst_K4g4tfdaergg6421";
		UsernamePasswordAuthenticationToken auth = getAuthenticationToken(13L);

		ResponseEntity<ProjectSideMenuResponse> response = sut.getProjectSideMenuDetails(projectToken, auth);

		assertThat(response.getBody().progress()).isEqualTo("60.0");
		assertThat(response.getBody().teamMembers().size()).isEqualTo(3);

	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 프로젝트_핵심결과_추가시_기대하는_응답을_리턴한다_keyResultToken() throws Exception {
		String projectToken = "mst_as3fg34tgg6421";
		String keyResultName = "keyResult";

		UsernamePasswordAuthenticationToken auth = getAuthenticationToken(13L);

		ResponseEntity<String> response = sut.registerKeyResult(
			new ProjectKeyResultSaveRequest(projectToken, keyResultName), auth);

		assertThat(response.getBody()).containsPattern(
			Pattern.compile("keyResult-[a-zA-Z0-9]{10}"));

	}

	@Test
	@Sql("classpath:insert-project-date.sql")
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
	@Sql("classpath:insert-project-date.sql")
	void 행동전략_완료시_기대하는_응답을_리턴한다() throws Exception {
		String initiativeToken = "ini_ixYjj5nODfeab3AH8";

		ResponseEntity<String> response = sut.initiativeFinished(initiativeToken, getAuthenticationToken(11L));

		assertThat(response.getBody()).isEqualTo("ini_ixYjj5nODfeab3AH8");
	}

	@Test
	@Sql("classpath:insert-project-date.sql")
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
	@Sql("classpath:insert-project-date.sql")
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
	@Sql("classpath:insert-project-date.sql")
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
	@Sql("classpath:insert-project-date.sql")
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
	@Sql("classpath:insert-project-date.sql")
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
	@Sql("classpath:insert-project-date.sql")
	void 년월로_getInitiativeDates를_호출하면_기대하는_응답을_리턴한다() throws Exception {
		String yearmonth = "2023-12";

		List<String> response =
			sut.getInitiativeDatesBy(
				yearmonth,
				getAuthenticationToken(15L)
			).getBody();

		assertThat(response.size()).isEqualTo(14);
		assertThat(response.get(0)).isEqualTo("2023-12-01");
		assertThat(response.get(response.size()-1)).isEqualTo("2023-12-14");

	}


	@Test
	@Sql("classpath:insert-project-date.sql")
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


	private UsernamePasswordAuthenticationToken getAuthenticationToken(long value) {
		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", value)
			.getSingleResult();

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			user, null, user.getAuthorities());
		return auth;
	}
}