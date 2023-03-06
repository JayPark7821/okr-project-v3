package kr.jay.okrver3.interfaces.project.response;

import java.time.LocalDate;

public record ProjectDetailResponse(
	String projectToken, boolean newProject, double progress, LocalDate sdt, LocalDate edt, int teamMembersCount,
	String projectType
) {
}
