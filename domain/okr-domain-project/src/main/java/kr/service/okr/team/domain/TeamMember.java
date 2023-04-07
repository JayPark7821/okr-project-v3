package kr.service.okr.team.domain;

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

	private TeamMember(
		final Long userSeq,
		final ProjectRoleType projectRoleType,
		final Project project
	) {
		this.userSeq = userSeq;
		this.projectRoleType = projectRoleType;
		this.project = project;
	}

	@Builder
	private TeamMember(final long userSeq, final Project project, final ProjectRoleType projectRoleType,
		final boolean isNew) {
		this.userSeq = userSeq;
		this.project = project;
		this.projectRoleType = projectRoleType;
		this.isNew = isNew;
	}

	public static TeamMember createLeader(long userSeq, Project project) {
		return new TeamMember(userSeq, ProjectRoleType.LEADER, project);
	}

	public static TeamMember createMember(long userSeq, Project project) {
		return new TeamMember(userSeq, ProjectRoleType.MEMBER, project);
	}

	public void deleteNewProjectMark() {
		this.isNew = Boolean.FALSE;
	}

}

