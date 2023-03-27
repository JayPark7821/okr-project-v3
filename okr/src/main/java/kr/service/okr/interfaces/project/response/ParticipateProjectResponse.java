package kr.service.okr.interfaces.project.response;

import kr.service.okr.domain.project.ProjectType;
import kr.service.okr.domain.project.aggregate.team.ProjectRoleType;

public record ParticipateProjectResponse(
	String projectToken,
	String objective,
	ProjectType projectType,
	ProjectRoleType roleType
) {
}
