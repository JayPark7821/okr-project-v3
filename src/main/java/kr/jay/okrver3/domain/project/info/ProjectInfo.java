package kr.jay.okrver3.domain.project.info;

import java.util.List;

import kr.jay.okrver3.domain.project.Project;


public record ProjectInfo(String projectToken, String objective, String startDate, String endDate,
						  String projectType, List<KeyResultInfo> keyResultInfos, int teamMembersCount) {

	public ProjectInfo(Project project) {
		this(project.getProjectToken(), project.getObjective(), project.getStartDate().toString(),
			project.getEndDate().toString(), project.getType().name(),
			project.getKeyResults().stream().map(KeyResultInfo::new).toList(),
			project.getTeamMember().size()
		);
	}
}

