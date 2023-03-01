package kr.jay.okrver3.interfaces.project;

import java.util.List;

import kr.jay.okrver3.domain.project.Project;

public record ProjectSideMenuResponse(String progress, List<ProjectTeamMemberResponse> teamMembers) {

	public ProjectSideMenuResponse(Project project) {
		this(String.valueOf(project.getProgress()),
			project.getTeamMember()
				.stream()
				.map(teamMember -> new ProjectTeamMemberResponse(teamMember.getUser()))
				.toList());
	}
}
