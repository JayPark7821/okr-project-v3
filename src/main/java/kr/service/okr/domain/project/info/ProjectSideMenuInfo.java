package kr.service.okr.domain.project.info;

import java.util.List;

import kr.service.okr.domain.project.Project;

public record ProjectSideMenuInfo(String progress, List<TeamMemberUserInfo> teamMembers) {
	public ProjectSideMenuInfo(Project project) {
		this(String.valueOf(project.getProgress()),
			project.getTeamMember()
				.stream()
				.map(teamMember -> new TeamMemberUserInfo(teamMember.getUser()))
				.toList());
	}

}
