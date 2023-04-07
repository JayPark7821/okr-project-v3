package kr.service.okr.project.aggregate.team.domain;

import kr.service.okr.model.project.team.ProjectRoleType;
import kr.service.okr.project.domain.Project;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TeamMember {
	private Long userSeq;
	private Project project;
	private ProjectRoleType projectRoleType;
	private boolean isNew = Boolean.TRUE;

	public TeamMember(
		final Long userSeq,
		final ProjectRoleType projectRoleType
	) {
		this.userSeq = userSeq;
		this.projectRoleType = projectRoleType;
	}

	@Builder
	private TeamMember(final long userSeq, final Project project, final ProjectRoleType projectRoleType,
		final boolean isNew) {
		this.userSeq = userSeq;
		this.project = project;
		this.projectRoleType = projectRoleType;
		this.isNew = isNew;
	}

	public static TeamMember createLeader(long userSeq) {
		return new TeamMember(userSeq, ProjectRoleType.LEADER);
	}

	public static TeamMember createMember(long userSeq) {
		return new TeamMember(userSeq, ProjectRoleType.MEMBER);
	}

	public void join(Project project) {
		this.project = project;
	}

	public void deleteNewProjectMark() {
		this.isNew = Boolean.FALSE;
	}

}
