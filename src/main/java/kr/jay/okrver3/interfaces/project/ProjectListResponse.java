package kr.jay.okrver3.interfaces.project;

import java.time.LocalDate;
import java.util.List;

public record ProjectListResponse(
	 String projectToken, boolean newProject, double progress, LocalDate sdt, LocalDate edt, List<ProjectTeamMemberResponse> teamMembers, String projectType
) {
}
