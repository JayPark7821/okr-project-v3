package kr.service.okr.project.domain;

import static kr.service.okr.exception.ErrorCode.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.model.project.ProjectType;
import kr.service.okr.model.project.team.ProjectRoleType;
import kr.service.okr.util.TokenGenerator;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Project {

	private static final String PROJECT_TOKEN_PREFIX = "project-";
	private static final int MAX_KEYRESULT_COUNT = 3;
	private static final int MAX_OBJECTIVE_LENGTH = 50;
	private static final int MAX_KERSULT_NAME_LENGTH = 50;
	private static final int MAX_INITIATIVE_NAME_LENGTH = 50;
	private static final int MAX_INITIATIVE_DETAIL_LENGTH = 200;

	private Long id;
	private String projectToken;
	private List<TeamMember> teamMember = new ArrayList<>();
	private List<KeyResult> keyResults = new ArrayList<>();
	private LocalDate startDate;
	private LocalDate endDate;
	private ProjectType type = ProjectType.SINGLE;
	private String objective;
	private double progress = 0.0D;
	private boolean finished = false;

	public Project(final String objective, final LocalDate startDate, final LocalDate endDate) {
		canRegisterProject(objective, startDate, endDate);

		this.projectToken = TokenGenerator.randomCharacterWithPrefix(PROJECT_TOKEN_PREFIX);
		this.startDate = startDate;
		this.endDate = endDate;
		this.objective = objective;
	}

	@Builder
	private Project(
		final Long id,
		final String projectToken,
		final List<TeamMember> teamMember,
		final List<KeyResult> keyResults,
		final LocalDate startDate,
		final LocalDate endDate,
		final ProjectType type,
		final String objective,
		final double progress,
		final boolean finished
	) {
		this.id = id;
		this.projectToken = projectToken;
		this.teamMember = teamMember == null ? new ArrayList<>() : teamMember;
		this.keyResults = keyResults;
		this.startDate = startDate;
		this.endDate = endDate;
		this.type = type;
		this.objective = objective;
		this.progress = progress;
		this.finished = finished;
	}

	public void createAndAddLeader(final Long leaderSeq) {
		canAddProjectLeader();

		final TeamMember leader = TeamMember.createLeader(leaderSeq, this);
		this.teamMember.add(leader);
	}

	public void createAndAddMemberOf(final Long memberSeq, final Long leaderSeq) {

		canAddNewTeamMember(memberSeq, leaderSeq);

		final TeamMember member = TeamMember.createMember(memberSeq, this);
		this.teamMember.add(member);
	}

	// TODO protected로 변경
	public void addTeamMember(final TeamMember member) {
		this.teamMember.add(member);
	}

	public String addKeyResult(final String keyResultName, final Long leader) {
		canRegisterKeyResult(keyResultName, leader);

		final KeyResult keyResult =
			new KeyResult(keyResultName, this.id, this.keyResults.size() + 1, new ArrayList<>());
		this.keyResults.add(keyResult);

		return keyResult.getKeyResultToken();
	}

	public void makeProjectFinished() {
		this.finished = true;
	}

	public String addInitiative(
		final String keyResultToken,
		final String initiativeName,
		final Long memberSeq,
		final String initiativeDetail,
		final LocalDate startDate,
		final LocalDate endDate
	) {
		canRegisterInitiative(initiativeName, initiativeDetail, startDate, endDate);

		final KeyResult keyResult = this.keyResults.stream()
			.filter(kr -> kr.getKeyResultToken().equals(keyResultToken))
			.findAny()
			.orElseThrow(() -> new OkrApplicationException(INVALID_KEYRESULT_TOKEN));

		final TeamMember member = this.teamMember.stream()
			.filter(teamMember -> teamMember.getUserSeq().equals(memberSeq))
			.findAny()
			.orElseThrow(() -> new OkrApplicationException(INVALID_PROJECT_TOKEN));

		return keyResult.addInitiative(initiativeName, member, initiativeDetail, startDate, endDate);
	}

	//====================================  validator  =================================================
	private void canRegisterInitiative(final String initiativeName, final String initiativeDetail,
		final LocalDate startDate,
		final LocalDate endDate) {
		validateProjectDuration();
		validateFinishedProject();

		Assert.notNull(initiativeName, INITIATIVE_NAME_IS_REQUIRED.getMessage());
		Assert.notNull(initiativeDetail, INITIATIVE_DETAIL_IS_REQUIRED.getMessage());
		Assert.isTrue(initiativeName.length() <= MAX_INITIATIVE_NAME_LENGTH && initiativeName.length() > 0,
			INITIATIVE_NAME_WRONG_INPUT_LENGTH.getMessage());
		Assert.isTrue(initiativeDetail.length() <= MAX_INITIATIVE_DETAIL_LENGTH && initiativeDetail.length() > 0,
			INITIATIVE_DETAIL_WRONG_INPUT_LENGTH.getMessage());
		Assert.notNull(startDate, INITIATIVE_START_DATE_IS_REQUIRED.getMessage());
		Assert.notNull(endDate, INITIATIVE_END_DATE_IS_REQUIRED.getMessage());

		if (endDate.isBefore(this.startDate) ||
			endDate.isAfter(this.endDate) ||
			startDate.isBefore(this.startDate) ||
			startDate.isAfter(this.endDate)
		) {
			throw new OkrApplicationException(ErrorCode.INVALID_INITIATIVE_DATE);
		}
	}

	private void canRegisterProject(final String objective, final LocalDate startDate, final LocalDate endDate) {
		Assert.notNull(objective, OBJECTIVE_IS_REQUIRED.getMessage());
		Assert.isTrue(objective.length() <= MAX_OBJECTIVE_LENGTH && objective.length() > 0,
			OBJECTIVE_WRONG_INPUT_LENGTH.getMessage());
		Assert.notNull(startDate, PROJECT_START_DATE_IS_REQUIRED.getMessage());
		Assert.notNull(endDate, PROJECT_END_DATE_IS_REQUIRED.getMessage());
		Assert.isTrue(startDate.isBefore(endDate), PROJECT_START_DATE_IS_AFTER_END_DATE.getMessage());
		Assert.isTrue(endDate.isAfter(LocalDate.now()), PROJECT_END_DATE_IS_BEFORE_TODAY.getMessage());
	}

	private void canAddNewTeamMember(final Long memberSeq, final Long leaderSeq) {
		validateFinishedProject();
		validateProjectDuration();
		validateProjectLeader(leaderSeq);

		if (memberSeq.equals(leaderSeq))
			throw new OkrApplicationException(ErrorCode.NOT_AVAIL_INVITE_MYSELF);

		if (this.teamMember.stream().anyMatch(member -> member.getUserSeq().equals(memberSeq)))
			throw new OkrApplicationException(ErrorCode.USER_ALREADY_PROJECT_MEMBER);
	}

	private void validateFinishedProject() {
		if (this.finished)
			throw new OkrApplicationException(ErrorCode.PROJECT_IS_FINISHED);
	}

	private void validateProjectLeader(final Long leaderSeq) {
		this.teamMember.stream()
			.filter(member ->
				member.getUserSeq().equals(leaderSeq) &&
					member.getProjectRoleType().equals(ProjectRoleType.LEADER))
			.findAny()
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.USER_IS_NOT_LEADER));
	}

	private void validateProjectDuration() {
		if (this.endDate.isBefore(LocalDate.now()))
			throw new OkrApplicationException(ErrorCode.NOT_UNDER_PROJECT_DURATION);
	}

	private void canRegisterKeyResult(final String keyResultName, final Long leader) {
		validateProjectLeader(leader);
		validateProjectDuration();
		validateFinishedProject();
		if (this.keyResults.size() >= MAX_KEYRESULT_COUNT)
			throw new OkrApplicationException(MAX_KEYRESULT_COUNT_EXCEEDED);

		if (keyResultName.length() >= MAX_KERSULT_NAME_LENGTH)
			throw new OkrApplicationException(KEYRESULT_NAME_WRONG_INPUT_LENGTH);
	}

	private void canAddProjectLeader() {
		validateFinishedProject();
		if (this.teamMember.stream()
			.anyMatch(teamMember -> teamMember.getProjectRoleType().equals(ProjectRoleType.LEADER)))
			throw new OkrApplicationException(ErrorCode.PROJECT_ALREADY_HAS_LEADER);
	}
}
