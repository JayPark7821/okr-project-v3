package kr.service.okr.project.domain;

import static java.time.LocalDate.*;
import static kr.service.okr.exception.ErrorCode.*;
import static kr.service.okr.project.domain.ProjectTest.ProjectStatusType.*;
import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.model.project.team.ProjectRoleType;
import kr.service.okr.project.usecase.RegisterProjectUseCase;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProjectTest {
	private static final Long LEADER = 1L;
	private static final Long FIRST_MEMBER = 2L;
	private static final Long SECOND_MEMBER = 3L;

	private RegisterProjectUseCase sut;

	@ParameterizedTest
	@MethodSource("projectConstructorTestSource")
	void 프로젝트_생성_실패_테스트_케이스(
		String objective,
		LocalDate startDate,
		LocalDate endDate,
		String errorMsg
	) throws Exception {

		assertThatThrownBy(() -> new Project(objective, startDate, endDate))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(errorMsg);
	}

	@Test
	void 프로젝트_생성_성공_테스트_케이스() throws Exception {
		assertThat(new Project("objective", generateDate(0), generateDate(10)).getProjectToken())
			.containsPattern(Pattern.compile("project-[a-zA-Z0-9]{12}"));
	}

	@ParameterizedTest
	@MethodSource("inviteTeamMemberTestSource")
	void 팀원_초대_실패_테스트_케이스(
		Project project,
		Long memberId,
		Long leaderId,
		String errorMsg
	) throws Exception {

		assertThatThrownBy(() -> project.createAndAddMemberOf(memberId, leaderId))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(errorMsg);
	}

	@Test
	void 팀원_초대_성공_테스트_케이스() throws Exception {
		final Project project = generateProject(NORMAL, 0, 0);

		project.createAndAddMemberOf(FIRST_MEMBER, LEADER);

		final List<TeamMember> teamMembers = project.getTeamMember();
		assertThat(teamMembers).hasSize(2);
		assertThat(teamMembers.stream()
			.filter(teamMember ->
				teamMember.getProjectRoleType().equals(ProjectRoleType.MEMBER) &&
					teamMember.getUserSeq().equals(2L)).findAny())
			.isPresent();
	}

	@ParameterizedTest
	@MethodSource("addKeyResultTestSource")
	void 핵심결과_생성_실패_테스트_케이스(
		Project project,
		Long userSeq,
		String errorMsg
	) throws Exception {
		final String keyResultName = "new keyResultName";
		assertThatThrownBy(() -> project.addKeyResult(keyResultName, userSeq))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(errorMsg);
	}

	@Test
	void 핵심결과_생성_성공_테스트_케이스() throws Exception {
		final Project project = generateProject(NORMAL, 0, 0);
		final String keyResultName = "new keyResultName";

		final String keyResultToken = project.addKeyResult(keyResultName, LEADER);

		assertThat(keyResultToken).containsPattern(Pattern.compile("keyresult-[a-zA-Z0-9]{10}"));
	}

	@ParameterizedTest
	@MethodSource("addInitiativeTestSource")
	void 행동전략_생성_실패_테스트_케이스(
		Project project,
		String keyResultToken,
		Long userSeq,
		LocalDate startDate,
		LocalDate endDate,
		String errorMsg
	) throws Exception {
		final String initiativeName = "new initiativeName";
		final String initiativeDetail = "initiative Details";
		assertThatThrownBy(
			() -> project.addInitiative(keyResultToken, initiativeName, userSeq, initiativeDetail, startDate,
				endDate))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(errorMsg);
	}

	@Test
	void 행동전략_생성_성공_테스트_케이스() throws Exception {
		final Project project = generateProject(NORMAL, 1, 1);
		final String keyResultToken = project.getKeyResults().get(0).getKeyResultToken();
		final String initiativeName = "new initiativeName";
		final String initiativeDetail = "initiative Details";

		assertThat(
			project.addInitiative(keyResultToken, initiativeName, FIRST_MEMBER, initiativeDetail, now(), now()))
			.containsPattern(Pattern.compile("initiative-[a-zA-Z0-9]{9}"));

		assertThat(project.getKeyResults()
			.stream()
			.filter(keyResult -> keyResult.getKeyResultToken().equals(keyResultToken))
			.count()).isEqualTo(1);

	}

	private static LocalDate generateDate(int days) {
		return days >= 0 ? now().plusDays(days) : now().minusDays(Math.abs(days));
	}

	private static Stream<Arguments> projectConstructorTestSource() {
		final String tooLongObjective = IntStream.range(0, 51)
			.mapToObj(i -> "a")
			.collect(Collectors.joining());

		return Stream.of(
			Arguments.of(null, generateDate(0), generateDate(10), OBJECTIVE_IS_REQUIRED.getMessage()),
			Arguments.of(tooLongObjective, generateDate(0), generateDate(10),
				OBJECTIVE_WRONG_INPUT_LENGTH.getMessage()),
			Arguments.of("objective", null, generateDate(10), PROJECT_START_DATE_IS_REQUIRED.getMessage()),
			Arguments.of("objective", generateDate(0), null, PROJECT_END_DATE_IS_REQUIRED.getMessage()),
			Arguments.of("objective", generateDate(10), generateDate(0),
				PROJECT_START_DATE_IS_AFTER_END_DATE.getMessage()),
			Arguments.of("objective", generateDate(-10), generateDate(-1),
				PROJECT_END_DATE_IS_BEFORE_TODAY.getMessage())
		);
	}

	private static Stream<Arguments> inviteTeamMemberTestSource() throws Exception {
		final Project project = generateProject(NORMAL, 0, 0);
		final Project endedProject = generateProject(ENDED, 0, 0);
		final Project finishedProject = generateProject(FINISHED, 0, 0);
		final Project projectWithMember = generateProject(NORMAL, 0, 0);
		projectWithMember.createAndAddMemberOf(FIRST_MEMBER, LEADER);

		return Stream.of(
			Arguments.of(endedProject, FIRST_MEMBER, LEADER, NOT_UNDER_PROJECT_DURATION.getMessage()),
			Arguments.of(finishedProject, FIRST_MEMBER, LEADER, PROJECT_IS_FINISHED.getMessage()),
			Arguments.of(project, LEADER, LEADER, NOT_AVAIL_INVITE_MYSELF.getMessage()),
			Arguments.of(project, SECOND_MEMBER, FIRST_MEMBER, USER_IS_NOT_LEADER.getMessage()),
			Arguments.of(projectWithMember, FIRST_MEMBER, LEADER, USER_ALREADY_PROJECT_MEMBER.getMessage())
		);
	}

	private static Stream<Arguments> addKeyResultTestSource() throws Exception {
		final Project project = generateProject(NORMAL, 0, 0);
		final Project endedProject = generateProject(ENDED, 0, 0);
		final Project finishedProject = generateProject(FINISHED, 0, 0);
		final Project projectWithKeyResult = generateProject(NORMAL, 0, 3);
		return Stream.of(
			Arguments.of(endedProject, LEADER, NOT_UNDER_PROJECT_DURATION.getMessage()),
			Arguments.of(finishedProject, LEADER, PROJECT_IS_FINISHED.getMessage()),
			Arguments.of(project, FIRST_MEMBER, USER_IS_NOT_LEADER.getMessage()),
			Arguments.of(projectWithKeyResult, LEADER, MAX_KEYRESULT_COUNT_EXCEEDED.getMessage())
		);
	}

	private static Stream<Arguments> addInitiativeTestSource() throws Exception {
		final Project project = generateProject(NORMAL, 1, 1);
		final Project endedProject = generateProject(ENDED, 1, 1);
		final Project finishedProject = generateProject(FINISHED, 1, 1);
		return Stream.of(
			Arguments.of(endedProject, endedProject.getKeyResults().get(0).getKeyResultToken(), FIRST_MEMBER,
				generateDate(0), generateDate(1), NOT_UNDER_PROJECT_DURATION.getMessage()),
			Arguments.of(finishedProject, finishedProject.getKeyResults().get(0).getKeyResultToken(), FIRST_MEMBER,
				generateDate(0), generateDate(1), PROJECT_IS_FINISHED.getMessage()),
			Arguments.of(project, project.getKeyResults().get(0).getKeyResultToken(), FIRST_MEMBER, generateDate(-11),
				generateDate(1), INVALID_INITIATIVE_DATE.getMessage()),
			Arguments.of(project, project.getKeyResults().get(0).getKeyResultToken(), FIRST_MEMBER, generateDate(0),
				generateDate(11), INVALID_INITIATIVE_DATE.getMessage()),
			Arguments.of(project, project.getKeyResults().get(0).getKeyResultToken(), FIRST_MEMBER, generateDate(0),
				generateDate(-11), INVALID_INITIATIVE_DATE.getMessage()),
			Arguments.of(project, project.getKeyResults().get(0).getKeyResultToken(), FIRST_MEMBER, generateDate(0),
				generateDate(11), INVALID_INITIATIVE_DATE.getMessage()),
			Arguments.of(project, project.getKeyResults().get(0).getKeyResultToken(), SECOND_MEMBER, generateDate(0),
				generateDate(0), INVALID_PROJECT_TOKEN.getMessage()),
			Arguments.of(project, "wrongKeyresultToken", SECOND_MEMBER, generateDate(0), generateDate(0),
				INVALID_KEYRESULT_TOKEN.getMessage())
		);
	}

	private static Project generateProject(ProjectStatusType type, int teamMemberCount, int keyResultCount) throws
		Exception {
		final Project project = new Project("objective", generateDate(-10), generateDate(10));
		project.createAndAddLeader(LEADER);

		LongStream.range(1, teamMemberCount + 1)
			.forEach(i -> project.createAndAddMemberOf(FIRST_MEMBER, i));

		IntStream.range(0, keyResultCount)
			.forEach(i -> project.addKeyResult("keyResultName" + i, LEADER));

		if (type.equals(FINISHED)) {
			project.makeProjectFinished();

		} else if (type.equals(ENDED)) {
			final Field endDate = project.getClass().getDeclaredField("endDate");
			endDate.setAccessible(true);
			endDate.set(project, generateDate(-1));
			endDate.setAccessible(false);
		}

		return project;
	}

	enum ProjectStatusType {
		NORMAL,
		FINISHED,
		ENDED;
	}
}