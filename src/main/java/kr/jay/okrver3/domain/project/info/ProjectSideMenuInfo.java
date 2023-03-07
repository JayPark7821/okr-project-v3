package kr.jay.okrver3.domain.project.info;

import java.util.List;

import kr.jay.okrver3.domain.project.Project;

public record ProjectSideMenuInfo(String progress, List<ProjectTeamMemberUserInfo> teamMembers) {
	public ProjectSideMenuInfo(Project project) {
		this(String.valueOf(project.getProgress()),
			project.getTeamMember()
				.stream()
				.map(teamMember -> new ProjectTeamMemberUserInfo(teamMember.getUser()))
				.toList());
	}

}
