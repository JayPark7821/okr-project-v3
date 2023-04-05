package kr.service.okr.project.domain;

import static kr.service.okr.OkrMessages.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import kr.service.okr.model.project.ProjectType;
import kr.service.okr.project.aggregate.keyresult.domain.KeyResult;
import kr.service.okr.project.aggregate.team.domain.TeamMember;
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
		Assert.hasText(objective, OBJECTIVE_IS_REQUIRED.getMsg());
		Assert.isTrue(objective.length() <= 50, OBJECTIVE_IS_TOO_LONG.getMsg());
		Assert.notNull(startDate, PROJECT_START_DATE_IS_REQUIRED.getMsg());
		Assert.notNull(endDate, PROJECT_END_DATE_IS_REQUIRED.getMsg());

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
		this.teamMember = teamMember;
		this.keyResults = keyResults;
		this.startDate = startDate;
		this.endDate = endDate;
		this.type = type;
		this.objective = objective;
		this.progress = progress;
	}

	public void addLeader(final Long leader) {
		this.teamMember.add(TeamMember.createLeader(leader, this));
	}

	public void addTeamMember(final Long teamMember) {
		this.teamMember.add(TeamMember.createMember(teamMember, this));
	}

	public void assignId(final Long id) {
		this.id = id;
	}
}
