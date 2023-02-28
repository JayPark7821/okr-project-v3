package kr.jay.okrver3.interfaces.project;

import java.time.LocalDate;
import java.util.List;

import kr.jay.okrver3.domain.project.service.ProjectDetailInfo;

public record ProjectDetailResponse(
	 String projectToken, boolean newProject, double progress, LocalDate sdt, LocalDate edt, List<ProjectTeamMemberResponse> teamMembers, String projectType
) {

	public ProjectDetailResponse(ProjectDetailInfo projectDetailInfo) {
		this(projectDetailInfo.projectToken(),
			projectDetailInfo.newProject(),
			projectDetailInfo.progress(),
			 projectDetailInfo.sdt(),
			projectDetailInfo.edt(),
			projectDetailInfo.teamMembers().projectTeamMemberUsers().stream().map(ProjectTeamMemberResponse::new).toList(),
			 projectDetailInfo.projectType().name());
	}
}
