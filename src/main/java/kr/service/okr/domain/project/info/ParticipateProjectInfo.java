package kr.service.okr.domain.project.info;

import kr.service.okr.common.exception.ErrorCode;
import kr.service.okr.common.exception.OkrApplicationException;
import kr.service.okr.domain.project.Project;
import kr.service.okr.domain.project.ProjectType;
import kr.service.okr.domain.project.aggregate.team.ProjectRoleType;

public record ParticipateProjectInfo(String projectToken, String objective, ProjectType projectType,
									 ProjectRoleType roleType) {

	public ParticipateProjectInfo(Project project, Long userSeq) {
		this(project.getProjectToken(),
			project.getObjective(),
			project.getType(),
			project.getTeamMember()
				.stream()
				.filter(teamMember -> teamMember.getUserSeq().equals(userSeq))
				.findFirst()
				.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_USER_INFO))
				.getProjectRoleType()
		);
	}
}
