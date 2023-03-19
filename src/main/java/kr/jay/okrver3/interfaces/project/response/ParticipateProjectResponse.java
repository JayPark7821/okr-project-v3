package kr.jay.okrver3.interfaces.project.response;

import kr.jay.okrver3.domain.project.ProjectType;
import kr.jay.okrver3.domain.project.aggregate.team.ProjectRoleType;

public record ParticipateProjectResponse(String projectToken, String objective, ProjectType projectType,
										 ProjectRoleType roleType) {
}
