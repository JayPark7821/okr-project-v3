package kr.jay.okrver3.domain.project.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.project.ProjectServiceImpl;
import kr.jay.okrver3.domain.project.ProjectType;
import kr.jay.okrver3.domain.project.SortType;
import kr.jay.okrver3.domain.project.aggregate.feedback.FeedbackType;
import kr.jay.okrver3.domain.project.aggregate.feedback.SearchRange;
import kr.jay.okrver3.domain.project.aggregate.initiative.Initiative;
import kr.jay.okrver3.domain.project.command.FeedbackSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectDetailRetrieveCommand;
import kr.jay.okrver3.domain.project.command.ProjectInitiativeSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectKeyResultSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectSaveCommand;
import kr.jay.okrver3.domain.project.info.FeedbackDetailInfo;
import kr.jay.okrver3.domain.project.info.FeedbackInfo;
import kr.jay.okrver3.domain.project.info.IniFeedbackInfo;
import kr.jay.okrver3.domain.project.info.InitiativeDetailInfo;
import kr.jay.okrver3.domain.project.info.InitiativeDoneInfo;
import kr.jay.okrver3.domain.project.info.InitiativeForCalendarInfo;
import kr.jay.okrver3.domain.project.info.InitiativeInfo;
import kr.jay.okrver3.domain.project.info.ProjectDetailInfo;
import kr.jay.okrver3.domain.project.info.ProjectInfo;
import kr.jay.okrver3.domain.project.info.ProjectSideMenuInfo;
import kr.jay.okrver3.domain.project.info.ProjectTeamMembersInfo;
import kr.jay.okrver3.domain.project.validator.InitiativeDoneValidator;
import kr.jay.okrver3.domain.project.validator.InitiativeInProgressValidator;
import kr.jay.okrver3.domain.project.validator.ProjectInitiativeDateValidator;
import kr.jay.okrver3.domain.project.validator.ProjectKeyResultCountValidator;
import kr.jay.okrver3.domain.project.validator.ProjectLeaderValidator;
import kr.jay.okrver3.domain.project.validator.ProjectPeriodValidator;
import kr.jay.okrver3.domain.project.validator.ProjectValidateProcessor;
import kr.jay.okrver3.domain.project.validator.SelfFeedbackValidator;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.infrastructure.project.ProjectQueryDslRepository;
import kr.jay.okrver3.infrastructure.project.ProjectRepositoryImpl;
import kr.jay.okrver3.infrastructure.project.aggregate.feedback.FeedbackQueryDslRepository;
import kr.jay.okrver3.infrastructure.project.aggregate.feedback.FeedbackRepositoryImpl;
import kr.jay.okrver3.infrastructure.project.aggregate.initiative.InitiativeQueryDslRepository;
import kr.jay.okrver3.infrastructure.project.aggregate.initiative.InitiativeRepositoryImpl;

@DataJpaTest
@Import({ProjectServiceImpl.class, ProjectRepositoryImpl.class, ProjectQueryDslRepository.class,
	ProjectValidateProcessor.class, ProjectLeaderValidator.class, InitiativeRepositoryImpl.class,
	FeedbackRepositoryImpl.class, ProjectKeyResultCountValidator.class, ProjectPeriodValidator.class,
	ProjectInitiativeDateValidator.class, InitiativeDoneValidator.class, InitiativeQueryDslRepository.class,
	InitiativeInProgressValidator.class, SelfFeedbackValidator.class, FeedbackQueryDslRepository.class
})
class ProjectServiceImplTest {

	@Autowired
	private ProjectServiceImpl sut;

	@PersistenceContext
	EntityManager em;

	private User getUser(long userSeq) {
		User user = em.createQuery("select u from User u where u.id = :userSeq", User.class)
			.setParameter("userSeq", userSeq)
			.getSingleResult();
		return user;
	}

	@Test
	@Sql("classpath:insert-user.sql")
	@DisplayName("팀원없이 프로젝트를 생성하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project() throws Exception {

		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		ProjectInfo projectInfo = sut.registerProject(
			new ProjectSaveCommand("projectObjective", projectSdt, projectEdt,
				List.of("keyResult1", "keyResult2"), null), 999L, List.of());

		assertThat(projectInfo.projectToken()).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));
		assertThat(projectInfo.keyResultInfos().get(0).name()).isEqualTo("keyResult1");
		assertThat(projectInfo.keyResultInfos().get(1).name()).isEqualTo("keyResult2");
		assertThat(projectInfo.projectType()).isEqualTo(ProjectType.SINGLE.name());
	}

	@Test
	@Sql("classpath:insert-user.sql")
	@DisplayName("팀원을 추가해 프로젝트를 생성하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project_with_team_members() throws Exception {

		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		ProjectInfo projectInfo = sut.registerProject(
			new ProjectSaveCommand("projectObjective", projectSdt, projectEdt,
				List.of("keyResult1", "keyResult2"), List.of("guest@email.com")), 999L, List.of(4L));

		assertThat(projectInfo.projectToken()).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));
		assertThat(projectInfo.keyResultInfos().get(0).name()).isEqualTo("keyResult1");
		assertThat(projectInfo.keyResultInfos().get(1).name()).isEqualTo("keyResult2");
		assertThat(projectInfo.projectType()).isEqualTo(ProjectType.TEAM.name());
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("projectToken으로 조회하면 기대하는 응답(ProjectResponse)을 반환한다.")
	void retrieve_project_with_project_token() throws Exception {

		ProjectInfo projectInfo = sut.getProjectInfoBy("project-fgFHxGWeIUQt", 999L);

		assertThat(projectInfo.projectToken()).isEqualTo("project-fgFHxGWeIUQt");
		assertThat(projectInfo.objective()).isEqualTo("projectObjective");
		assertThat(projectInfo.startDate()).isEqualTo("2020-12-01");
		assertThat(projectInfo.endDate()).isEqualTo("3999-12-12");
		assertThat(projectInfo.projectType()).isEqualTo("SINGLE");
	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("팀원 추가를 시도하면 기대하는 응답(추가된 email주소)을 반환한다.")
	void invite_team_member() throws Exception {

		ProjectTeamMembersInfo response = sut.inviteTeamMember("project-fgFHxGWeIUQt", 998L, 999L);

		assertThat(response.teamMemberSeq().size()).isEqualTo(3);

	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("팀원 추가를 위해 email을 입력하면 기대하는 응답(email)을 반환한다.")
	void validate_email_address() throws Exception {

		sut.validateUserToInvite("project-fgFHxGWeIUQt", 996L, 999L);

	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("로그인한 유저가 속하지 않은 프로젝트에 팀원 추가를 위해 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_with_not_participating_project_throw_exception() throws Exception {

		assertThatThrownBy(() -> sut.validateUserToInvite("project-fgFHxGWeIUQt", 996L, 998L))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.INVALID_PROJECT_TOKEN.getMessage());

	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("리더가 아닌 팀원이 팀원 추가를 위해 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void when_member_validate_email_address_will_throw_exception() throws Exception {

		assertThatThrownBy(() -> sut.validateUserToInvite("project-fgFHxGWeIUQt", 996L, 997L))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_IS_NOT_LEADER.getMessage());

	}

	@Test
	@Sql({"classpath:insert-user.sql", "classpath:insert-project.sql", "classpath:insert-team.sql"})
	@DisplayName("이미 팀에 초대된 팀원의 email을 입력하면 기대하는 응답(exception)을 반환한다.")
	void validate_email_address_already_team_member() throws Exception {

		assertThatThrownBy(() -> sut.validateUserToInvite("project-fgFHxGWeIUQt", 997L, 999L))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_ALREADY_PROJECT_MEMBER.getMessage());
	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 메인_페이지_프로젝트_조회시_조건에_따라_기대하는_응답을_리턴한다_최근생성순_종료된프로젝트_미포함_팀프로젝트() throws Exception {

		List<String> recentlyCreatedSortProject = List.of("mst_3gbyy554frgg6421", "mst_K4232g4g5rgg6421");

		Page<ProjectDetailInfo> result = sut.getDetailProjectList(
			new ProjectDetailRetrieveCommand(SortType.RECENTLY_CREATE, ProjectType.TEAM, "N",
				PageRequest.of(0, 5)), 13L);

		assertThat(result.getTotalElements()).isEqualTo(2);
		List<ProjectDetailInfo> content = result.getContent();

		for (int i = 0; i < content.size(); i++) {
			ProjectDetailInfo r = content.get(i);
			assertThat(r.projectType()).isEqualTo(ProjectType.TEAM.name());
			assertThat(r.progress()).isLessThan(100);
			assertThat(r.projectToken()).isEqualTo(recentlyCreatedSortProject.get(i));
		}

	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 프로젝트_사이드_메뉴_조회시_기대하는_응답을_리턴한다_progress_team_members() throws Exception {
		String projectToken = "mst_K4g4tfdaergg6421";

		ProjectSideMenuInfo response = sut.getProjectSideMenuDetails(projectToken, 13L);

		assertThat(response.progress()).isEqualTo("60.0");
		assertThat(response.teamMembers().size()).isEqualTo(3);

	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 프로젝트_핵심결과_추가시_기대하는_응답을_리턴한다_keyResultToken() throws Exception {
		String projectToken = "mst_as3fg34tgg6421";
		String keyResultName = "keyResult";

		String response = sut.registerKeyResult(new ProjectKeyResultSaveCommand(projectToken, keyResultName), 2L);

		assertThat(response).containsPattern(
			Pattern.compile("keyResult-[a-zA-Z0-9]{10}"));

	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 프로젝트_기간_종료후_핵심결과_추가시_기대하는_응답을_리턴한다_exception() throws Exception {
		String projectToken = "mst_K4e8a5s7d6lb6421";
		String keyResultName = "keyResult";

		assertThatThrownBy(
			() -> sut.registerKeyResult(new ProjectKeyResultSaveCommand(projectToken, keyResultName), 12L))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.NOT_UNDER_PROJECT_DURATION.getMessage());

	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 팀원이_프로젝트_핵심결과_추가시_기대하는_응답을_리턴한다_exception() throws Exception {
		String projectToken = "mst_as3fg34tgg6421";
		String keyResultName = "keyResult";

		assertThatThrownBy(
			() -> sut.registerKeyResult(new ProjectKeyResultSaveCommand(projectToken, keyResultName), 3L))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_IS_NOT_LEADER.getMessage());
	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 프로젝트에_핵심결과_3개_이상_추가시_기대하는_응답을_리턴한다_exception() throws Exception {
		String projectToken = "mst_Kiwqnp1Nq6lbTNn0";
		String keyResultName = "keyResult";

		assertThatThrownBy(
			() -> sut.registerKeyResult(new ProjectKeyResultSaveCommand(projectToken, keyResultName), 2L))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.KEYRESULT_LIMIT_EXCEED.getMessage());

	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 행동전략_추가시_기대하는_응답을_리턴한다_initiativeToken() throws Exception {

		ProjectInitiativeSaveCommand requestDto = new ProjectInitiativeSaveCommand(
			"key_wV6MX15WQ3DTzQMs",
			"행동전략",
			LocalDate.now().minusDays(10),
			LocalDate.now().plusDays(10),
			"행동전략 상세내용"
		);

		String response = sut.registerInitiative(requestDto, 3L);

		Initiative initiativeToken = em.createQuery(
				"select i from Initiative i where i.initiativeToken = :initiativeToken", Initiative.class)
			.setParameter("initiativeToken", response)
			.getSingleResult();
		assertThat(initiativeToken.getInitiativeToken()).containsPattern(
			Pattern.compile("initiative-[a-zA-Z0-9]{9}"));
	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 행동전략_추가시_프로젝트_진척도_변경된다() throws Exception {
		//Given
		ProjectInitiativeSaveCommand requestDto = new ProjectInitiativeSaveCommand(
			"key_wV6MX15WQ3DTzQMs",
			"행동전략",
			LocalDate.now().minusDays(10),
			LocalDate.now().plusDays(10),
			"행동전략 상세내용"
		);

		//When
		String response = sut.registerInitiative(requestDto, 3L);

		//Then
		Project project = em.createQuery(
				"select p from Project p where p.id = :id", Project.class)
			.setParameter("id", 99998L)
			.getSingleResult();

		assertThat(project.getProgress()).isEqualTo(50.0);
	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 행동전략_완료시_기대하는_응답을_리턴한다() throws Exception {
		String initiativeToken = "ini_ixYjj5nODfeab3AH8";

		InitiativeDoneInfo response = sut.initiativeFinished(initiativeToken, 11L);

		Initiative initiative = em.createQuery(
				"select i from Initiative i where i.id = :id", Initiative.class)
			.setParameter("id", 99998L)
			.getSingleResult();
		assertThat(initiative.isDone()).isTrue();
		assertThat(response.initiativeToken()).isEqualTo("ini_ixYjj5nODfeab3AH8");
	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 종료된_프로젝트의_행동전략_완료시_기대하는_응답Exception을_리턴한다() throws Exception {
		String initiativeToken = "ini_iefefawef3fdab3AH8";

		assertThatThrownBy(() -> sut.initiativeFinished(initiativeToken, 15L))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.NOT_UNDER_PROJECT_DURATION.getMessage());
	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 이미_종료된_행동전략_완료_요청시_기대하는_응답Exception을_리턴한다() throws Exception {
		String initiativeToken = "ini_ixYjj5aaafeab3AH8";

		assertThatThrownBy(() -> sut.initiativeFinished(initiativeToken, 11L))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.FINISHED_INITIATIVE.getMessage());
	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 행동전략_완료시_프로젝트_진척도_update() throws Exception {
		String initiativeToken = "ini_ixYjj5nODfeab3AH8";

		InitiativeDoneInfo response = sut.initiativeFinished(initiativeToken, 11L);

		Project project = em.createQuery(
				"select p from Project p where p.id = :id", Project.class)
			.setParameter("id", 99997L)
			.getSingleResult();
		assertThat(project.getProgress()).isEqualTo(100.0);
	}

	@Test
	@Sql("classpath:insert-project-date.sql")
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
	@Sql("classpath:insert-project-date.sql")
	void 팀원의_행동전략에_피드백을_추가하면_기대하는_응답을_리턴한다() throws Exception {

		FeedbackSaveCommand command =
			new FeedbackSaveCommand("피드백 작성", "GOOD_IDEA",
				"ini_ixYjj5aaafeab3AH8");

		FeedbackInfo response =
			sut.registerFeedback(
				command,
				3L
			);

		assertThat(response.feedbackToken()).containsPattern(
			Pattern.compile("feedback-[a-zA-Z0-9]{11}"));
	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 종료된_프로젝트의_행동전략에_피드백을_추가하면_기대하는_응답을_리턴한다_exception() throws Exception {

		FeedbackSaveCommand command =
			new FeedbackSaveCommand("피드백 작성", "GOOD_IDEA",
				"ini_iefefena3fdab3AH8");

		assertThatThrownBy(() -> sut.registerFeedback(command, 7L))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.NOT_UNDER_PROJECT_DURATION.getMessage());
	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 자신의_행동전략에_피드백을_추가하면_기대하는_응답을_리턴한다_exception() throws Exception {

		FeedbackSaveCommand command =
			new FeedbackSaveCommand("피드백 작성", "GOOD_IDEA",
				"ini_ixYjjnnnafeab3AH8");

		assertThatThrownBy(() -> sut.registerFeedback(command, 3L))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.MOT_AVAIL_FEEDBACK_SELF.getMessage());
	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 완료전의_행동전략에_피드백을_추가하면_기대하는_응답을_리턴한다_exception() throws Exception {

		FeedbackSaveCommand command =
			new FeedbackSaveCommand("피드백 작성", "GOOD_IDEA",
				"ini_ixYjj5nODfeab3AH8");

		assertThatThrownBy(() -> sut.registerFeedback(command, 3L))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.NOT_FINISHED_INITIATIVE.getMessage());
	}

	@Test
	@Sql("classpath:insert-project-date.sql")
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
	@Sql("classpath:insert-project-date.sql")
	void 다른팀원의_행동전략토큰으로_getInitiativeBy호출시_기대하는_응답_InitiativeDetailInfo를_리턴한다() throws Exception {
		String initiativeToken = "ini_ixYjj5nODfeab3AH8";

		InitiativeDetailInfo response =
			sut.getInitiativeBy(initiativeToken, 3L);

		assertThat(response.done()).isFalse();
		assertThat(response.initiativeToken()).isEqualTo(initiativeToken);
		assertThat(response.initiativeName()).isEqualTo("ini name222");
		assertThat(response.initiativeDetail()).isEqualTo("initiative detail222");
		assertThat(response.myInitiative()).isFalse();
		assertThat(response.user().userEmail()).isEqualTo("keyResultTest@naver.com");
	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 잘못된_행동전략토큰으로_getInitiativeBy호출시_기대하는_응답_InitiativeDetailInfo를_리턴한다() throws Exception {
		String initiativeToken = "ini_ixYjj5nOfefeAH8";

		assertThatThrownBy(() -> sut.getInitiativeBy(initiativeToken, 3L))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.INVALID_INITIATIVE_TOKEN.getMessage());
	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 날짜로_getInitiativeByDate를_호출하면_기대하는_응답InitiativeForCalendarResponse를_size1_리턴한다() throws Exception {
		LocalDate date = LocalDate.of(2022, 12, 01);

		List<InitiativeForCalendarInfo> response =
			sut.getInitiativeByDate(date, 14L);

		assertThat(response.size()).isEqualTo(1);
		assertThat(response.get(0).initiativeToken()).isEqualTo("ini_ixYjj5na3fdab3AH8");
		assertThat(response.get(0).initiativeName()).isEqualTo("ini name876");
		assertThat(response.get(0).startDate()).isEqualTo("2000-12-12");
		assertThat(response.get(0).endDate()).isEqualTo("2023-12-14");
	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 날짜로_getInitiativeByDate를_호출하면_기대하는_응답InitiativeForCalendarResponse를_size3_리턴한다() throws Exception {
		LocalDate date = LocalDate.of(2022, 12, 01);

		List<InitiativeForCalendarInfo> response =
			sut.getInitiativeByDate(date, 15L);

		assertThat(response.size()).isEqualTo(3);

	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 년월로_getInitiativeDates를_호출하면_기대하는_응답을_리턴한다() throws Exception {
		YearMonth yearmonth = YearMonth.of(2023, 12);

		List<String> response =
			sut.getInitiativeDatesBy(yearmonth, 15L);

		assertThat(response.size()).isEqualTo(14);
		assertThat(response.get(0)).isEqualTo("2023-12-01");
		assertThat(response.get(response.size() - 1)).isEqualTo("2023-12-14");

	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 자신의_행동전략토큰으로_getInitiativeFeedbacksBy를_호출하면_기대하는_응답IniFeedbackResponse를_리턴한다() throws Exception {
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
	@Sql("classpath:insert-project-date.sql")
	void 피드백X_팀원의_행동전략토큰으로_getInitiativeFeedbacksBy를_호출하면_기대하는_응답IniFeedbackResponse를_리턴한다() throws Exception {
		String initiativeToken = "ini_ixYjj5nODqtb3AH8";

		IniFeedbackInfo response =
			sut.getInitiativeFeedbacksBy(initiativeToken, 4L);

		assertThat(response.myInitiative()).isFalse();
		assertThat(response.wroteFeedback()).isFalse();
		assertThat(response.feedback().size()).isEqualTo(2);

	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void 피드백을남긴_팀원의_행동전략토큰으로_getInitiativeFeedbacksBy를_호출하면_기대하는_응답IniFeedbackResponse를_리턴한다() throws Exception {
		String initiativeToken = "ini_ixYjj5nODqtb3AH8";

		IniFeedbackInfo response =
			sut.getInitiativeFeedbacksBy(initiativeToken, 2L);

		assertThat(response.myInitiative()).isFalse();
		assertThat(response.wroteFeedback()).isTrue();
		assertThat(response.feedback().size()).isEqualTo(2);
		assertThat(response.gradeCount().get(FeedbackType.BEST_RESULT).longValue()).isEqualTo(1L);
		assertThat(response.gradeCount().get(FeedbackType.BURNING_PASSION).longValue()).isEqualTo(1);

	}

	@Test
	@Sql("classpath:insert-project-date.sql")
	void getCountOfInitiativeToGiveFeedback을_호출하면_아직_피드백을_남기지않은_팀원의_완료된_행동전략count를_리턴한다() throws Exception {

		Integer response = sut.getCountOfInitiativeToGiveFeedback(3L);

		assertThat(response).isEqualTo(1);
	}

	@Test
	@Sql("classpath:insert-project-date.sql")
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

}