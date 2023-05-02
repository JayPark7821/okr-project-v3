package kr.service.okr.project.domain;

import static java.time.LocalDate.*;
import static kr.service.okr.project.domain.ProjectTestParameters.ProjectStatusType.*;
import static kr.service.okr.project.domain.ProjectTestParameters.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.project.domain.enums.ProjectRoleType;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProjectTest {
	static final Long LEADER = 1L;
	static final Long FIRST_MEMBER = 2L;
	static final Long SECOND_MEMBER = 3L;

	@ParameterizedTest
	@MethodSource("kr.service.okr.project.domain.ProjectTestParameters#projectConstructorTestSource")
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
	@MethodSource("kr.service.okr.project.domain.ProjectTestParameters#inviteTeamMemberTestSource")
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
	@MethodSource("kr.service.okr.project.domain.ProjectTestParameters#addKeyResultTestSource")
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

		final KeyResult keyResult = project.addKeyResult(keyResultName, LEADER);

		assertThat(keyResult.getKeyResultToken()).containsPattern(Pattern.compile("keyResult-[a-zA-Z0-9]{10}"));
	}

	@ParameterizedTest
	@MethodSource("kr.service.okr.project.domain.ProjectTestParameters#addInitiativeTestSource")
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
			() -> project.addInitiative(keyResultToken, initiativeName, initiativeDetail, startDate, endDate, userSeq
			))
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
			project.addInitiative(keyResultToken, initiativeName, initiativeDetail, now(), now(), FIRST_MEMBER)
				.getInitiativeToken())
			.containsPattern(Pattern.compile("initiative-[a-zA-Z0-9]{9}"));

		assertThat(project.getKeyResults()
			.stream()
			.filter(keyResult -> keyResult.getKeyResultToken().equals(keyResultToken))
			.count()).isEqualTo(1);

	}

	@Test
	void 프로젝트_업데이트_성공_테스트_케이스() throws Exception {
		final Project project = generateProject(NORMAL, 1, 1);
		final String keyResultToken = project.getKeyResults().get(0).getKeyResultToken();
		project.addInitiative(keyResultToken, "new initiativeName", "initiative Details", now(), now(), FIRST_MEMBER);

		final String updatedObject = "updated Object";
		project.updateProject(updatedObject, generateDate(0), generateDate(10), LEADER);
		assertThat(project.getObjective()).isEqualTo(updatedObject);
		assertThat(project.getStartDate()).isEqualTo(generateDate(0));
		assertThat(project.getEndDate()).isEqualTo(generateDate(10));
	}

	@ParameterizedTest
	@MethodSource("kr.service.okr.project.domain.ProjectTestParameters#updateProjectTestSource")
	void 프로젝트_업데이트_실패_테스트_케이스(
		Project project,
		Long userSeq,
		LocalDate startDate,
		LocalDate endDate,
		String objective,
		String errorMsg
	) throws Exception {
		assertThatThrownBy(() -> project.updateProject(objective, startDate, endDate, userSeq))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(errorMsg);
	}

	@Test
	void 핵심결과_업데이트_성공_테스트_케이스() throws Exception {
		//given
		final Project project = generateProject(NORMAL, 1, 1);
		final KeyResult keyResult = project.getKeyResults().get(0);
		final String updatedKeyResultName = "updated keyResultName";

		//when
		project.updateKeyResult(keyResult.getKeyResultToken(), updatedKeyResultName, LEADER);

		//then
		assertThat(keyResult.getName()).isEqualTo(updatedKeyResultName);
	}

	@ParameterizedTest
	@MethodSource("kr.service.okr.project.domain.ProjectTestParameters#updateKeyResultTestSource")
	void 핵심결과_업데이트_실패_테스트_케이스(
		Project project,
		Long userSeq,
		String keyResultToken,
		String keyResultName,
		String errorMsg
	) throws Exception {
		assertThatThrownBy(() -> project.updateKeyResult(keyResultToken, keyResultName, userSeq))
			.isInstanceOf(OkrApplicationException.class)
			.hasMessage(errorMsg);
	}

}