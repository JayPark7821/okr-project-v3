package kr.jay.okrver3.interfaces.project;

import java.time.LocalDate;

import kr.jay.okrver3.domain.project.service.ProjectDetailInfo;

public record ProjectDetailResponse(
	String projectToken, boolean newProject, double progress, LocalDate sdt, LocalDate edt, int teamMembersCount,
	String projectType
) {

	public ProjectDetailResponse(ProjectDetailInfo projectDetailInfo) {
		this(projectDetailInfo.projectToken(),
			projectDetailInfo.newProject(),
			projectDetailInfo.progress(),
			projectDetailInfo.sdt(),
			projectDetailInfo.edt(),
			projectDetailInfo.teamMemberCount(),
			projectDetailInfo.projectType());
	}
}
