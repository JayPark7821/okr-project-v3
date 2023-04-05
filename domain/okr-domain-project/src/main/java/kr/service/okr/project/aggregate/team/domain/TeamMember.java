package kr.service.okr.project.aggregate.team.domain;

import kr.service.okr.model.project.team.ProjectRoleType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TeamMember {
	private long userSeq;
	private Long projectId;
	private ProjectRoleType projectRoleType;
	private boolean isNew = Boolean.FALSE;

	public TeamMember(
		final long userSeq,
		final ProjectRoleType projectRoleType
	) {
		this.userSeq = userSeq;
		this.projectRoleType = projectRoleType;
	}

	@Builder
	private TeamMember(final long userSeq, final Long projectId, final ProjectRoleType projectRoleType,
		final boolean isNew) {
		this.userSeq = userSeq;
		this.projectId = projectId;
		this.projectRoleType = projectRoleType;
		this.isNew = isNew;
	}

	public static TeamMember createLeader(long userSeq) {
		return new TeamMember(userSeq, ProjectRoleType.LEADER);
	}

	public static TeamMember createMember(long userSeq) {
		return new TeamMember(userSeq, ProjectRoleType.MEMBER);
	}

}

