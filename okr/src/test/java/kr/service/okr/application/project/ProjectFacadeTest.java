package kr.service.okr.application.project;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.service.okr.domain.notification.Notification;
import kr.service.okr.domain.notification.NotificationServiceImpl;
import kr.service.okr.domain.notification.Notifications;
import kr.service.okr.domain.project.Project;
import kr.service.okr.domain.project.ProjectAsyncService;
import kr.service.okr.domain.project.ProjectServiceImpl;
import kr.service.okr.domain.project.ProjectType;
import kr.service.okr.domain.project.SortType;
import kr.service.okr.domain.project.aggregate.feedback.SearchRange;
import kr.service.okr.domain.project.aggregate.team.ProjectRoleType;
import kr.service.okr.domain.project.command.FeedbackSaveCommand;
import kr.service.okr.domain.project.command.ProjectDetailRetrieveCommand;
import kr.service.okr.domain.project.command.ProjectInitiativeSaveCommand;
import kr.service.okr.domain.project.command.ProjectKeyResultSaveCommand;
import kr.service.okr.domain.project.command.ProjectSaveCommand;
import kr.service.okr.domain.project.command.TeamMemberInviteCommand;
import kr.service.okr.domain.project.info.FeedbackDetailInfo;
import kr.service.okr.domain.project.info.IniFeedbackInfo;
import kr.service.okr.domain.project.info.InitiativeDetailInfo;
import kr.service.okr.domain.project.info.InitiativeForCalendarInfo;
import kr.service.okr.domain.project.info.InitiativeInfo;
import kr.service.okr.domain.project.info.ParticipateProjectInfo;
import kr.service.okr.domain.project.info.ProjectDetailInfo;
import kr.service.okr.domain.project.info.ProjectInfo;
import kr.service.okr.domain.project.info.ProjectSideMenuInfo;
import kr.service.okr.domain.project.validator.InitiativeDoneValidator;
import kr.service.okr.domain.project.validator.InitiativeInProgressValidator;
import kr.service.okr.domain.project.validator.ProjectInitiativeDateValidator;
import kr.service.okr.domain.project.validator.ProjectKeyResultCountValidator;
import kr.service.okr.domain.project.validator.ProjectLeaderValidator;
import kr.service.okr.domain.project.validator.ProjectPeriodValidator;
import kr.service.okr.domain.project.validator.ProjectValidateProcessor;
import kr.service.okr.domain.project.validator.SelfFeedbackValidator;
import kr.service.okr.domain.user.User;
import kr.service.okr.domain.user.service.impl.UserServiceImpl;
import kr.service.okr.infrastructure.notification.NotificationJDBCRepository;
import kr.service.okr.infrastructure.notification.NotificationQueryDslRepository;
import kr.service.okr.infrastructure.notification.NotificationRepositoryImpl;
import kr.service.okr.infrastructure.project.ProjectQueryDslRepository;
import kr.service.okr.infrastructure.project.ProjectRepositoryImpl;
import kr.service.okr.infrastructure.project.aggregate.feedback.FeedbackQueryDslRepository;
import kr.service.okr.infrastructure.project.aggregate.feedback.FeedbackRepositoryImpl;
import kr.service.okr.infrastructure.project.aggregate.initiative.InitiativeQueryDslRepository;
import kr.service.okr.infrastructure.project.aggregate.initiative.InitiativeRepositoryImpl;
import kr.service.okrcommon.common.exception.ErrorCode;
import kr.service.okrcommon.common.exception.OkrApplicationException;

@DataJpaTest
@Import({ProjectFacade.class, ProjectServiceImpl.class, UserServiceImpl.class,
	NotificationServiceImpl.class, NotificationJDBCRepository.class, ProjectRepositoryImpl.class,
	ProjectQueryDslRepository.class, ProjectValidateProcessor.class, ProjectLeaderValidator.class,
	ProjectKeyResultCountValidator.class, ProjectPeriodValidator.class, ProjectInitiativeDateValidator.class,
	InitiativeRepositoryImpl.class, FeedbackRepositoryImpl.class, InitiativeDoneValidator.class,
	InitiativeQueryDslRepository.class, NotificationRepositoryImpl.class, SelfFeedbackValidator.class,
	InitiativeInProgressValidator.class, FeedbackQueryDslRepository.class, NotificationQueryDslRepository.class,
	ProjectAsyncService.class
})
class ProjectFacadeTest {

	@Autowired
	private ProjectFacade sut;

	@PersistenceContext
	EntityManager em;

	private User getUser(Long seq) {
		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", seq)
			.getSingleResult();
		return user;
	}

	@Test
	@Sql("classpath:insert-user.sql")
	@DisplayName("팀원 추가 없이 프로젝트를 생성하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project() throws Exception {

		User user = getUser(999L);

		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		String projectToken = sut.registerProject(
			new ProjectSaveCommand("projectObjective", projectSdt, projectEdt, null), user.getUserSeq());

		Project result =
			em.createQuery("select n from Project n where n.objective =: objective", Project.class)
				.setParameter("objective", "projectObjective")
				.getSingleResult();

		assertThat(result.getTeamMember().size()).isEqualTo(1);
		assertThat(projectToken).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));
	}

	@Test
	@Sql("classpath:insert-user.sql")
	@DisplayName("프로젝트를 생성시 팀원을 같이 입력하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project_with_team_members() throws Exception {

		User user = getUser(999L);

		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		String projectToken = sut.registerProject(
			new ProjectSaveCommand("projectObjective", projectSdt, projectEdt, List.of("guest@email.com")),
			user.getUserSeq());

		Project result =
			em.createQuery("select n from Project n where n.objective =: objective", Project.class)
				.setParameter("objective", "projectObjective")
				.getSingleResult();

		assertThat(result.getTeamMember().size()).isEqualTo(2);

		assertThat(projectToken).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("projectToken으로 조회하면 기대하는 응답(ProjectResponse)을 반환한다.")
	void retrieve_project_with_project_token() throws Exception {

		User user = getUser(999L);

		ProjectInfo projectInfo = sut.getProjectInfoBy("project-fgFHxGWeIUQt", user.getUserSeq());

		assertThat(projectInfo.projectToken()).isEqualTo("project-fgFHxGWeIUQt");
		assertThat(projectInfo.objective()).isEqualTo("projectObjective");
		assertThat(projectInfo.startDate()).isEqualTo("2020-12-01");
		assertThat(projectInfo.endDate()).isEqualTo("3999-12-12");
		assertThat(projectInfo.projectType()).isEqualTo("SINGLE");
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("팀원 추가를 시도하면 기대하는 응답(추가된 email주소)을 반환하고 알림을 저장한다.")
	void invite_team_member() throws Exception {

		String response = sut.inviteTeamMember(
			new TeamMemberInviteCommand("project-fgFHxGWeIUQt", "fakeAppleEmail"), 999L);

		assertThat(response).isEqualTo("fakeAppleEmail");

		List<Notification> result = em.createQuery("select n from Notification n", Notification.class)
			.getResultList();
		assertThat(result.size()).isEqualTo(2);
		assertThat(result.get(0).getMsg()).isEqualTo(
			Notifications.NEW_TEAM_MATE.getMsg("fakeAppleName", "projectObjective"));

	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("팀원 추가를 위해 email을 입력하면 기대하는 응답(email)을 반환한다.")
	void validate_email_address() throws Exception {

		String memberEmail = "guest@email.com";
		User user = getUser(999L);

		final String response = sut.validateEmailToInvite("project-fgFHxGWeIUQt", memberEmail, user.getUserSeq());

		assertThat(response).isEqualTo(memberEmail);
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("로그인한 유저가 속하지 않은 프로젝트에 팀원 추가를 위해 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_with_not_participating_project_throw_exception() throws Exception {

		String memberEmail = "guest@email.com";
		User user = getUser(998L);

		assertThatThrownBy(() -> sut.validateEmailToInvite("project-fgFHxGWeIUQt", memberEmail, user.getUserSeq()))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.INVALID_PROJECT_TOKEN.getMessage());

	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("리더가 아닌 팀원이 팀원 추가를 위해 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void when_member_validate_email_address_will_throw_exception() throws Exception {
		String memberEmail = "guest@email.com";
		User user = getUser(997L);

		assertThatThrownBy(() -> sut.validateEmailToInvite("project-fgFHxGWeIUQt", memberEmail, user.getUserSeq()))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_IS_NOT_LEADER.getMessage());

	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("팀원 추가를 위해 잘못된 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_exception() throws Exception {

		String wrongEmailAdd = "wrongEmailAdd";
		User user = getUser(999L);

		assertThatThrownBy(() -> sut.validateEmailToInvite("project-fgFHxGWeIUQt", wrongEmailAdd, user.getUserSeq()))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.INVALID_USER_EMAIL.getMessage());
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("이미 팀에 초대된 팀원의 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_already_team_member() throws Exception {
		String teamMemberEmail = "fakeGoogleIdEmail";
		User user = getUser(999L);

		assertThatThrownBy(() -> sut.validateEmailToInvite("project-fgFHxGWeIUQt", teamMemberEmail, user.getUserSeq()))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_ALREADY_PROJECT_MEMBER.getMessage());
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("로그인된 유저 자신의 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_login_user_email() throws Exception {
		String userEmail = "apple@apple.com";
		User user = getUser(999L);

		assertThatThrownBy(() -> sut.validateEmailToInvite("project-fgFHxGWeIUQt", userEmail, user.getUserSeq()))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.NOT_AVAIL_INVITE_MYSELF.getMessage());
	}

	@Test
	@Sql("classpath:insert-project-data.sql")
	void 메인_페이지_프로젝트_조회시_조건에_따라_기대하는_응답을_리턴한다_최근생성순_종료된프로젝트_미포함_팀프로젝트() throws Exception {

		List<String> recentlyCreatedSortProject =
			List.of("mst_K4g4tfdaergg6421", "mst_3gbyy554frgg6421", "mst_K4232g4g5rgg6421");
		;
		User user = getUser(13L);

		Page<ProjectDetailInfo> result = sut.getDetailProjectList(
			new ProjectDetailRetrieveCommand(SortType.RECENTLY_CREATE, ProjectType.TEAM, "N",
				PageRequest.of(0, 5)), user.getUserSeq());

		assertThat(result.getTotalElements()).isEqualTo(3);
		List<ProjectDetailInfo> content = result.getContent();

		for (int i = 0; i < content.size(); i++) {
			ProjectDetailInfo r = content.get(i);
			assertThat(r.projectType()).isEqualTo(ProjectType.TEAM.name());
			assertThat(r.progress()).isLessThan(100);
			assertThat(r.projectToken()).isEqualTo(recentlyCreatedSortProject.get(i));
		}

	}

	@Test
	@Sql("classpath:insert-project-data.sql")
	void 프로젝트_사이드_메뉴_조회시_기대하는_응답을_리턴한다_progress_team_members() throws Exception {
		String projectToken = "mst_K4g4tfdaergg6421";
		User user = getUser(13L);

		ProjectSideMenuInfo response = sut.getProjectSideMenuDetails(projectToken, user.getUserSeq());

		assertThat(response.progress()).isEqualTo("60.0");
		assertThat(response.teamMembers().size()).isEqualTo(3);

	}

	@Test
	@Sql("classpath:insert-project-data.sql")
	void 프로젝트_핵심결과_추가시_기대하는_응답을_리턴한다_keyResultToken() throws Exception {
		String projectToken = "mst_as3fg34tgg6421";
		String keyResultName = "keyResult";

		User user = getUser(2L);

		String response = sut.registerKeyResult(new ProjectKeyResultSaveCommand(projectToken, keyResultName),
			user.getUserSeq());

		assertThat(response).containsPattern(
			Pattern.compile("keyResult-[a-zA-Z0-9]{10}"));

	}

	@Test
	@Sql("classpath:insert-project-data.sql")
	void 팀원이_프로젝트_핵심결과_추가시_기대하는_응답을_리턴한다_exception() throws Exception {
		String projectToken = "mst_K4g4tfdaergg6421";
		String keyResultName = "keyResult";

		User user = getUser(13L);

		assertThatThrownBy(
			() -> sut.registerKeyResult(new ProjectKeyResultSaveCommand(projectToken, keyResultName),
				user.getUserSeq()))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_IS_NOT_LEADER.getMessage());
	}

	@Test
	@Sql("classpath:insert-project-data.sql")
	void 프로젝트에_핵심결과_3개_이상_추가시_기대하는_응답을_리턴한다_exception() throws Exception {
		String projectToken = "mst_Kiwqnp1Nq6lbTNn0";
		String keyResultName = "keyResult";

		User user = getUser(2L);

		assertThatThrownBy(
			() -> sut.registerKeyResult(new ProjectKeyResultSaveCommand(projectToken, keyResultName),
				user.getUserSeq()))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.KEYRESULT_LIMIT_EXCEED.getMessage());

	}

	@Test
	@Sql("classpath:insert-project-data.sql")
	void 행동전략_추가시_기대하는_응답을_리턴한다_initiativeToken() throws Exception {

		ProjectInitiativeSaveCommand requestDto = new ProjectInitiativeSaveCommand(
			"key_wV6MX15WQ3DTzQMs",
			"행동전략",
			LocalDate.now().minusDays(10),
			LocalDate.now().plusDays(10),
			"행동전략 상세내용"
		);

		User user = getUser(3L);

		String response = sut.registerInitiative(requestDto, user.getUserSeq());

		assertThat(response).containsPattern(
			Pattern.compile("initiative-[a-zA-Z0-9]{9}"));
	}

	@Test
	@Sql("classpath:insert-project-data.sql")
	void 행동전략_추가시_프로젝트_진척도_변경된다() throws Exception {
		//Given
		ProjectInitiativeSaveCommand requestDto = new ProjectInitiativeSaveCommand(
			"key_wV6MX15WQ3DTzQMs",
			"행동전략",
			LocalDate.now().minusDays(10),
			LocalDate.now().plusDays(10),
			"행동전략 상세내용"
		);

		User user = getUser(3L);

		//When
		String response = sut.registerInitiative(requestDto, user.getUserSeq());

		//Then
		Project project = em.createQuery(
				"select p from Project p where p.id = :id", Project.class)
			.setParameter("id", 99998L)
			.getSingleResult();
		Thread.sleep(100L);

		assertThat(project.getProgress()).isEqualTo(50.0);
	}

	@Test
	@Sql("classpath:insert-project-data.sql")
	void 행동전략_완료시_기대하는_응답을_리턴한다() throws Exception {
		String initiativeToken = "ini_ixYjj5nODfeab3AH8";

		String response = sut.initiativeFinished(initiativeToken, getUser(11L).getUserSeq());

		List<Notification> notifications =
			em.createQuery("select n from Notification n where n.msg =: msg", Notification.class)
				.setParameter("msg", Notifications.INITIATIVE_ACHIEVED.getMsg("testUser2", "ini name222"))
				.getResultList();

		assertThat(notifications.size()).isEqualTo(1);
		assertThat(response).isEqualTo("ini_ixYjj5nODfeab3AH8");
	}

	@Test
	@Sql("classpath:insert-project-data.sql")
	void 핵심결과토큰으로_행동전략_리스트_조회시_기대하는_응답을_리턴한다() throws Exception {
		String keyResultToken = "key_wV6f45vWQaaazQaa";
		List<String> savedInitiativeTokenRecentlyCreatedOrder = List.of("ini_ixYjj5nODfeab3AH8",
			"ini_ixYjj5aaafeab3AH8", "ini_ixYjjnnnafeab3AH8");

		Page<InitiativeInfo> response =
			sut.getInitiativeByKeyResultToken(keyResultToken, 11L, PageRequest.of(0, 5));

		assertThat(response.getTotalElements()).isEqualTo(3);
		List<InitiativeInfo> content = response.getContent();

		for (int i = 0; i < content.size(); i++) {
			assertThat(content.get(i).initiativeToken()).isEqualTo(savedInitiativeTokenRecentlyCreatedOrder.get(i));
		}

	}

	@Test
	@Sql("classpath:insert-project-data.sql")
	void 팀원의_행동전략에_피드백을_추가하면_기대하는_응답을_리턴한다() throws Exception {

		FeedbackSaveCommand command =
			new FeedbackSaveCommand("피드백 작성", "GOOD_IDEA",
				"ini_ixYjj5aaafeab3AH8");

		String response =
			sut.registerFeedback(
				command,
				3L
			);

		assertThat(response).containsPattern(
			Pattern.compile("feedback-[a-zA-Z0-9]{11}"));
	}

	@Test
	@Sql("classpath:insert-project-data.sql")
	void 팀원의_행동전략에_피드백을_추가하면_행동전략을_작성한_팀원에게_알림이_전송된다() throws Exception {

		FeedbackSaveCommand command =
			new FeedbackSaveCommand("피드백 작성", "GOOD_IDEA",
				"ini_ixYjj5aaafeab3AH8");

		String response =
			sut.registerFeedback(
				command,
				3L
			);

		Notification result =
			em.createQuery("select n from Notification n where n.user.id =: userId", Notification.class)
				.setParameter("userId", 11L)
				.getSingleResult();

		assertThat(result.getType()).isEqualTo(Notifications.NEW_FEEDBACK);

	}

	@Test
	@Sql("classpath:insert-project-data.sql")
	void 행동전략토큰으로_getInitiativeBy호출시_기대하는_응답_InitiativeDetailInfo를_리턴한다() throws Exception {
		String initiativeToken = "ini_ixYjj5nODqtb3AH8";

		InitiativeDetailInfo response =
			sut.getInitiativeBy(initiativeToken, 3L);

		assertThat(response.done()).isTrue();
		assertThat(response.initiativeToken()).isEqualTo(initiativeToken);
		assertThat(response.initiativeName()).isEqualTo("ini name");
		assertThat(response.initiativeDetail()).isEqualTo("initiative detail1");
		assertThat(response.myInitiative()).isTrue();
		assertThat(response.user().userEmail()).isEqualTo("user1@naver.com");
	}

	@Test
	@Sql("classpath:insert-project-data.sql")
	void 날짜로_getInitiativeByDate를_호출하면_기대하는_응답InitiativeForCalendarResponse를_size1_리턴한다() throws Exception {
		LocalDate date = LocalDate.of(2023, 12, 01);

		List<InitiativeForCalendarInfo> response =
			sut.getInitiativeByDate(date, 14L);

		assertThat(response.size()).isEqualTo(1);
		assertThat(response.get(0).initiativeToken()).isEqualTo("ini_ixYjj5na3fdab3AH8");
		assertThat(response.get(0).initiativeName()).isEqualTo("ini name876");
		assertThat(response.get(0).startDate()).isEqualTo("2000-12-12");
		assertThat(response.get(0).endDate()).isEqualTo("2023-12-14");
	}

	@Test
	@Sql("classpath:insert-project-data.sql")
	void 년월로_getInitiativeDates를_호출하면_기대하는_응답을_리턴한다() throws Exception {
		YearMonth yearmonth = YearMonth.of(2023, 12);

		List<String> response =
			sut.getInitiativeDatesBy(yearmonth, 15L);

		assertThat(response.size()).isEqualTo(14);
		assertThat(response.get(0)).isEqualTo("2023-12-01");
		assertThat(response.get(response.size() - 1)).isEqualTo("2023-12-14");

	}

	@Test
	@Sql("classpath:insert-project-data.sql")
	void 행동전략토큰으로_getInitiativeFeedbacksBy를_호출하면_기대하는_응답IniFeedbackResponse를_리턴한다() throws Exception {
		String initiativeToken = "ini_ixYjj5nODqtb3AH8";

		IniFeedbackInfo response =
			sut.getInitiativeFeedbacksBy(initiativeToken, 3L);

		assertThat(response.myInitiative()).isTrue();
		assertThat(response.wroteFeedback()).isFalse();
		assertThat(response.feedback().size()).isEqualTo(2);
		assertThat(response.feedback().get(0).feedbackToken()).isEqualTo("feedback_el6q34zazzSyWx9");
		assertThat(response.feedback().get(1).feedbackToken()).isEqualTo("feedback_aaaaaagawe3rfwa3");

	}

	@Test
	@Sql("classpath:insert-project-data.sql")
	void getCountOfInitiativeToGiveFeedback을_호출하면_아직_피드백을_남기지않은_팀원의_완료된_행동전략count를_리턴한다() throws Exception {

		Integer response = sut.getCountOfInitiativeToGiveFeedback(3L);

		assertThat(response).isEqualTo(1);
	}

	@Test
	@Sql("classpath:insert-project-data.sql")
	void getRecievedFeedback을_호출하면_기대한는_응답page_FeedbackDetailResponse를_리턴한다() throws Exception {
		List<String> feedbackTokenList = List.of("feedback_aaaaaagawe3rfwa3", "feedback_el6q34zazzSyWx9");

		SearchRange searchRange = SearchRange.ALL;
		Page<FeedbackDetailInfo> response = sut.getRecievedFeedback(
			searchRange,
			3L,
			PageRequest.of(0, 5)
		);

		assertThat(response.getTotalElements()).isEqualTo(2);
		List<FeedbackDetailInfo> content = response.getContent();

		for (int i = 0; i < content.size(); i++) {
			FeedbackDetailInfo r = content.get(i);
			assertThat(r.feedbackToken()).isEqualTo(feedbackTokenList.get(i));
		}
	}

	@Test
	@Sql("classpath:insert-project-data.sql")
	void 회원가입_탈퇴전_참여중인_프로젝트_리스트를_요청하면_기대하는_응답을_리턴한다_ParticipateProjectResponse() throws Exception {

		final List<ParticipateProjectInfo> response = sut.getParticipateProjects(3L);

		assertThat(response.size()).isEqualTo(8);
		assertThat(
			response.stream()
				.filter(t -> t.roleType().equals(ProjectRoleType.LEADER))
				.toList()
				.size()
		).isEqualTo(4);
	}

	@Test
	@Sql("classpath:insert-project-data.sql")
	void getRequiredFeedbackInitiative을_호출하면_기대한는_응답_ProjectInitiativeResponse를_리턴한다() throws Exception {
		final List<InitiativeInfo> response = sut.getRequiredFeedbackInitiative(3L);

		assertThat(response.size()).isEqualTo(1);
		assertThat(response.get(0).initiativeToken()).isEqualTo("ini_ixYjj5aaafeab3AH8");
	}
}