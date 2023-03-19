package kr.jay.okrver3.domain.project.info;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.project.ProjectType;
import kr.jay.okrver3.domain.project.aggregate.team.ProjectRoleType;

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
