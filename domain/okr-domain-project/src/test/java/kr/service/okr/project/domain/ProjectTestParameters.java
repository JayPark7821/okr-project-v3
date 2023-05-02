package kr.service.okr.project.domain;

import static java.time.LocalDate.*;
import static kr.service.okr.exception.ErrorCode.*;
import static kr.service.okr.project.domain.ProjectTest.*;
import static kr.service.okr.project.domain.ProjectTestParameters.ProjectStatusType.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

public class ProjectTestParameters {

	static LocalDate generateDate(int days) {
		return days >= 0 ? now().plusDays(days) : now().minusDays(Math.abs(days));
	}

	static Stream<Arguments> updateKeyResultTestSource() throws Exception {
		final String tooLongKeyResult = IntStream.range(0, 51)
			.mapToObj(i -> "a")
			.collect(Collectors.joining());

		final Project project = generateProject(NORMAL, 0, 1);
		final Project endedProject = generateProject(ENDED, 0, 1);
		final Project finishedProject = generateProject(FINISHED, 0, 1);
		final Project projectWithMember = generateProject(NORMAL, 0, 1);
		projectWithMember.createAndAddMemberOf(FIRST_MEMBER, LEADER);

		return Stream.of(
			Arguments.of(endedProject, LEADER, endedProject.getKeyResults().get(0).getKeyResultToken(),
				"new keyResultName", NOT_UNDER_PROJECT_DURATION.getMessage()),
			Arguments.of(finishedProject, LEADER, endedProject.getKeyResults().get(0).getKeyResultToken(),
				"new keyResultName", PROJECT_IS_FINISHED.getMessage()),
			Arguments.of(projectWithMember, FIRST_MEMBER, endedProject.getKeyResults().get(0).getKeyResultToken(),
				"new keyResultName", USER_IS_NOT_LEADER.getMessage()),
			Arguments.of(project, LEADER, endedProject.getKeyResults().get(0).getKeyResultToken(), tooLongKeyResult,
				KEYRESULT_NAME_WRONG_INPUT_LENGTH.getMessage()),
			Arguments.of(project, LEADER, "not availableKeyResultToken", tooLongKeyResult,
				KEYRESULT_NAME_WRONG_INPUT_LENGTH.getMessage())
		);
	}

	static Stream<Arguments> projectConstructorTestSource() {
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

	static Stream<Arguments> inviteTeamMemberTestSource() throws Exception {
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

	static Stream<Arguments> addKeyResultTestSource() throws Exception {
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

	static Stream<Arguments> addInitiativeTestSource() throws Exception {
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

	static Stream<Arguments> updateProjectTestSource() throws Exception {
		final Project project = generateProject(NORMAL, 0, 0);
		final Project finishedProject = generateProject(FINISHED, 0, 0);
		final String tooLongObjective = IntStream.range(0, 51)
			.mapToObj(i -> "a")
			.collect(Collectors.joining());
		final Project projectWithInitiative = generateProject(NORMAL, 1, 1);
		final String keyResultToken = projectWithInitiative.getKeyResults().get(0).getKeyResultToken();
		projectWithInitiative.addInitiative(keyResultToken, "new initiativeName", "initiative Details",
			generateDate(-10), generateDate(10), FIRST_MEMBER
		);

		return Stream.of(
			Arguments.of(finishedProject, LEADER, generateDate(0), generateDate(10), "new objective",
				PROJECT_IS_FINISHED.getMessage()),
			Arguments.of(project, FIRST_MEMBER, generateDate(0), generateDate(10), "new objective",
				USER_IS_NOT_LEADER.getMessage()),
			Arguments.of(project, LEADER, generateDate(0), generateDate(10), "",
				OBJECTIVE_WRONG_INPUT_LENGTH.getMessage()),
			Arguments.of(project, LEADER, generateDate(0), generateDate(10), tooLongObjective,
				OBJECTIVE_WRONG_INPUT_LENGTH.getMessage()),
			Arguments.of(project, LEADER, generateDate(11), generateDate(10), "new objective",
				PROJECT_START_DATE_IS_AFTER_END_DATE.getMessage()),
			Arguments.of(project, LEADER, generateDate(11), null, "new objective",
				PROJECT_START_DATE_IS_AFTER_END_DATE.getMessage()),
			Arguments.of(project, LEADER, null, generateDate(-20), "new objective",
				PROJECT_START_DATE_IS_AFTER_END_DATE.getMessage()),
			Arguments.of(projectWithInitiative, LEADER, generateDate(5), generateDate(6), "new objective",
				INITIATIVE_DATES_WILL_BE_OVER_PROJECT_DATES.getMessage()),
			Arguments.of(projectWithInitiative, LEADER, null, generateDate(6), "new objective",
				INITIATIVE_DATES_WILL_BE_OVER_PROJECT_DATES.getMessage()),
			Arguments.of(projectWithInitiative, LEADER, generateDate(5), null, "new objective",
				INITIATIVE_DATES_WILL_BE_OVER_PROJECT_DATES.getMessage())
		);
	}

	static Project generateProject(ProjectStatusType type, int teamMemberCount, int keyResultCount) throws
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
