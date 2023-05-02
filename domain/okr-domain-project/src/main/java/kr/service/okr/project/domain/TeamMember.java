package kr.service.okr.project.domain;

import static kr.service.okr.exception.ErrorCode.*;

import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.project.domain.enums.ProjectRoleType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TeamMember {
	private Long userSeq;
	private Long projectId;
	private ProjectRoleType projectRoleType;
	private boolean isNew = Boolean.TRUE;

	private TeamMember(
		final Long userSeq,
		final ProjectRoleType projectRoleType,
		final Long projectId
	) {
		this.userSeq = userSeq;
		this.projectRoleType = projectRoleType;
		this.projectId = projectId;
	}

	@Builder
	private TeamMember(
		final Long userSeq,
		final Long projectId,
		final ProjectRoleType projectRoleType,
		final boolean isNew
	) {
		if (userSeq == null || projectId == null) {
			throw new OkrApplicationException(ID_IS_REQUIRED);
		}
		this.userSeq = userSeq;
		this.projectId = projectId;
		this.projectRoleType = projectRoleType;
		this.isNew = isNew;
	}

	static TeamMember createLeader(Long userSeq, Long projectId) {
		return new TeamMember(userSeq, ProjectRoleType.LEADER, projectId);
	}

	static TeamMember createMember(Long userSeq, Long projectId) {
		return new TeamMember(userSeq, ProjectRoleType.MEMBER, projectId);
	}

	public void deleteNewProjectMark() {
		this.isNew = Boolean.FALSE;
	}

}

