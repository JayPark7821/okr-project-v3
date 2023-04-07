package kr.service.okr.project.domain;

import static kr.service.okr.exception.ErrorCode.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.keyresult.domain.KeyResult;
import kr.service.okr.model.project.ProjectType;
import kr.service.okr.model.project.team.ProjectRoleType;
import kr.service.okr.team.domain.TeamMember;
import kr.service.okr.util.TokenGenerator;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Project {

	private static final String PROJECT_TOKEN_PREFIX = "project-";
	private static final int MAX_KEYRESULT_COUNT = 3;
	private Long id;
	private String projectToken;
	private List<TeamMember> teamMember = new ArrayList<>();
	private List<KeyResult> keyResults = new ArrayList<>();
	private LocalDate startDate;
	private LocalDate endDate;
	private ProjectType type = ProjectType.SINGLE;
	private String objective;
	private double progress = 0.0D;

	public Project(final String objective, final LocalDate startDate, final LocalDate endDate) {
		Assert.hasText(objective, OBJECTIVE_IS_REQUIRED.getMessage());
		Assert.isTrue(objective.length() <= 50, OBJECTIVE_IS_TOO_LONG.getMessage());
		Assert.notNull(startDate, PROJECT_START_DATE_IS_REQUIRED.getMessage());
		Assert.notNull(endDate, PROJECT_END_DATE_IS_REQUIRED.getMessage());
		Assert.isTrue(startDate.isBefore(endDate), PROJECT_START_DATE_IS_AFTER_END_DATE.getMessage());
		Assert.isTrue(endDate.isAfter(LocalDate.now()), PROJECT_END_DATE_IS_BEFORE_TODAY.getMessage());

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
		final double progress
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
	}

	public void createAndAddLeaderOf(final Long leaderSeq) {
		final TeamMember leader = TeamMember.createLeader(leaderSeq, this);
		this.teamMember.add(leader);
	}

	public void createAndAddMemberOf(final Long memberSeq, final Long leaderSeq) {

		if (this.endDate.isAfter(LocalDate.now()))
			throw new OkrApplicationException(ErrorCode.NOT_UNDER_PROJECT_DURATION);

		if (memberSeq.equals(leaderSeq))
			throw new OkrApplicationException(ErrorCode.NOT_AVAIL_INVITE_MYSELF);

		this.teamMember.stream()
			.filter(member ->
				member.getUserSeq().equals(leaderSeq) &&
					member.getProjectRoleType().equals(ProjectRoleType.LEADER))
			.findAny()
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.USER_IS_NOT_LEADER));

		this.teamMember.stream()
			.filter(member -> member.getUserSeq().equals(leaderSeq))
			.findAny()
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.USER_ALREADY_PROJECT_MEMBER));

		final TeamMember member = TeamMember.createMember(memberSeq, this);
		this.teamMember.add(member);
	}

	public void addTeamMember(final TeamMember member) {
		this.teamMember.add(member);
	}

}
