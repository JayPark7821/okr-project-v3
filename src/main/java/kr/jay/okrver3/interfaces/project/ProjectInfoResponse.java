package kr.jay.okrver3.interfaces.project;

import kr.jay.okrver3.domain.project.service.ProjectInfo;

public record ProjectInfoResponse(String projectToken, String name, String objective, String startDate, String endDate,
								  String projectType) {
	public ProjectInfoResponse(ProjectInfo projectInfo) {
		this(projectInfo.projectToken(), projectInfo.name(), projectInfo.objective(), projectInfo.startDate(),
			projectInfo.endDate(), projectInfo.projectType());
	}
}
