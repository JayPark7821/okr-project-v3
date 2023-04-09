package kr.service.okr.project.domain;

import static kr.service.okr.project.domain.ProjectTest.ProjectStatusType.*;
import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.model.project.team.ProjectRoleType;
import kr.service.okr.project.usecase.RegisterProjectUseCase;
import kr.service.okr.team.domain.TeamMember;

class ProjectTest {
	private static final Long LEADER = 1L;
	private static final Long FIRST_MEMBER = 2L;
	private static final Long SECOND_MEMBER = 3L;

	@Value("${project.objective.max-length}")
	private RegisterProjectUseCase sut;

	@Test
	void project_constructor_test() throws Exception {
		final String tooLongObjective = IntStream.range(0, 51)
			.mapToObj(i -> "a")
			.collect(Collectors.joining());

		assertThatThrownBy(() -> new Project(null, generateDate(0), generateDate(10)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorCode.OBJECTIVE_IS_REQUIRED.getMessage());

		assertThatThrownBy(() -> new Project(tooLongObjective, generateDate(0), generateDate(10)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorCode.OBJECTIVE_WRONG_INPUT_LENGTH.getMessage());

		assertThatThrownBy(() -> new Project("objective", null, generateDate(10)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorCode.PROJECT_START_DATE_IS_REQUIRED.getMessage());

		assertThatThrownBy(() -> new Project("objective", generateDate(0), null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorCode.PROJECT_END_DATE_IS_REQUIRED.getMessage());

		assertThatThrownBy(() -> new Project("objective", generateDate(10), generateDate(0)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorCode.PROJECT_START_DATE_IS_AFTER_END_DATE.getMessage());

		assertThatThrownBy(() -> new Project("objective", generateDate(-10), generateDate(-1)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorCode.PROJECT_END_DATE_IS_BEFORE_TODAY.getMessage());

		assertThat(new Project("objective", generateDate(0), generateDate(10)).getProjectToken())
			.containsPattern(Pattern.compile("project-[a-zA-Z0-9]{12}"));
	}

	@Test
	void inviteNewTeamMember() throws Exception {
		// given
		final Project project = generateProject(NORMAL, 0, 0);
		final Project endedProject = generateProject(ENDED, 0, 0);
		final Project finishedProject = generateProject(FINISHED, 0, 0);

		// when
		assertThatThrownBy(() -> endedProject.createAndAddMemberOf(FIRST_MEMBER, LEADER))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.NOT_UNDER_PROJECT_DURATION.getMessage());

		assertThatThrownBy(() -> finishedProject.createAndAddMemberOf(FIRST_MEMBER, LEADER))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.PROJECT_IS_FINISHED.getMessage());

		assertThatThrownBy(() -> project.createAndAddMemberOf(LEADER, LEADER))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.NOT_AVAIL_INVITE_MYSELF.getMessage());

		assertThatThrownBy(() -> project.createAndAddMemberOf(SECOND_MEMBER, FIRST_MEMBER))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_IS_NOT_LEADER.getMessage());

		assertThatThrownBy(() -> project.createAndAddMemberOf(FIRST_MEMBER, SECOND_MEMBER))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_IS_NOT_LEADER.getMessage());

		project.createAndAddMemberOf(FIRST_MEMBER, LEADER);

		assertThatThrownBy(() -> project.createAndAddMemberOf(FIRST_MEMBER, LEADER))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_ALREADY_PROJECT_MEMBER.getMessage());

		//then
		final List<TeamMember> teamMembers = project.getTeamMember();
		assertThat(teamMembers).hasSize(2);
		assertThat(teamMembers.stream()
			.filter(teamMember ->
				teamMember.getProjectRoleType().equals(ProjectRoleType.MEMBER) &&
					teamMember.getUserSeq().equals(2L)).findAny())
			.isPresent();

	}

	@Test
	void registerKeyResult() throws Exception {
		//given
		final Project project = generateProject(NORMAL, 0, 0);
		final Project endedProject = generateProject(ENDED, 0, 0);
		final Project finishedProject = generateProject(FINISHED, 0, 0);
		final String keyResultName = "new keyResultName";
		//when
		assertThatThrownBy(() -> endedProject.addKeyResult(keyResultName, LEADER))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.NOT_UNDER_PROJECT_DURATION.getMessage());

		assertThatThrownBy(() -> finishedProject.addKeyResult(keyResultName, LEADER))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.PROJECT_IS_FINISHED.getMessage());

		assertThatThrownBy(() -> project.addKeyResult(keyResultName, FIRST_MEMBER))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.USER_IS_NOT_LEADER.getMessage());

		IntStream.range(0, 3)
			.forEach(i -> project.addKeyResult(keyResultName + i, LEADER));

		assertThatThrownBy(() -> project.addKeyResult(keyResultName, LEADER))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.MAX_KEYRESULT_COUNT_EXCEEDED.getMessage());

		//then
		assertThat(project.getKeyResults()).hasSize(3);
	}

	@Test
	void registerInitiative() throws Exception {
		//given
		final Project project = generateProject(NORMAL, 1, 1);
		final Project endedProject = generateProject(ENDED, 1, 1);
		final Project finishedProject = generateProject(FINISHED, 1, 1);
		final String initiativeName = "new initiativeName";
		final String initiativeDetail = "initiative Details";
		final String projectKeyResultToken = project.getKeyResults().get(0).getKeyResultToken();
		final String endedProjectKeyResultToken = endedProject.getKeyResults().get(0).getKeyResultToken();
		final String finishedProjectKeyResultToken = finishedProject.getKeyResults().get(0).getKeyResultToken();

		//when
		assertThatThrownBy(() -> endedProject.addInitiative(
			endedProjectKeyResultToken,
			initiativeName,
			FIRST_MEMBER,
			initiativeDetail,
			LocalDate.now(),
			LocalDate.now().plusDays(1))
		)
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.NOT_UNDER_PROJECT_DURATION.getMessage());

		assertThatThrownBy(
			() -> finishedProject.addInitiative(
				finishedProjectKeyResultToken,
				initiativeName,
				FIRST_MEMBER,
				initiativeDetail,
				LocalDate.now(),
				LocalDate.now().plusDays(1))
		)
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.PROJECT_IS_FINISHED.getMessage());

		assertThatThrownBy(
			() -> project.addInitiative(
				projectKeyResultToken,
				initiativeName,
				FIRST_MEMBER,
				initiativeDetail,
				LocalDate.now().minusDays(11),
				LocalDate.now().plusDays(1))
		)
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.INVALID_INITIATIVE_DATE.getMessage());

		assertThatThrownBy(
			() -> project.addInitiative(
				projectKeyResultToken,
				initiativeName,
				FIRST_MEMBER,
				initiativeDetail,
				LocalDate.now().minusDays(0),
				LocalDate.now().plusDays(11))
		)
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.INVALID_INITIATIVE_DATE.getMessage());

		assertThatThrownBy(
			() -> project.addInitiative(
				projectKeyResultToken,
				initiativeName,
				FIRST_MEMBER,
				initiativeDetail,
				LocalDate.now().minusDays(0),
				LocalDate.now().minusDays(11))
		)
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.INVALID_INITIATIVE_DATE.getMessage());

		assertThatThrownBy(
			() -> project.addInitiative(
				projectKeyResultToken,
				initiativeName,
				FIRST_MEMBER,
				initiativeDetail,
				LocalDate.now().minusDays(0),
				LocalDate.now().plusDays(11))
		)
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.INVALID_INITIATIVE_DATE.getMessage());

		assertThatThrownBy(
			() -> project.addInitiative(
				projectKeyResultToken,
				initiativeName,
				SECOND_MEMBER,
				initiativeDetail,
				LocalDate.now().minusDays(0),
				LocalDate.now().plusDays(11))
		)
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.INVALID_PROJECT_TOKEN.getMessage());

		assertThatThrownBy(
			() -> project.addInitiative(
				"wrongKeyresultToken",
				initiativeName,
				SECOND_MEMBER,
				initiativeDetail,
				LocalDate.now().minusDays(0),
				LocalDate.now().plusDays(11))
		)
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(ErrorCode.INVALID_PROJECT_TOKEN.getMessage());

		project.addInitiative(projectKeyResultToken, initiativeName, FIRST_MEMBER, initiativeDetail, LocalDate.now(),
			LocalDate.now());

		//then
		assertThat(project.getKeyResults()
			.stream()
			.filter(keyResult -> keyResult.getKeyResultToken().equals(projectKeyResultToken))
			.count()).isEqualTo(1);

	}

	private LocalDate generateDate(int days) {
		return days >= 0 ? LocalDate.now().plusDays(days) : LocalDate.now().minusDays(Math.abs(days));
	}

	public Project generateProject(ProjectStatusType type, int teamMemberCount, int keyResultCount) throws Exception {
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