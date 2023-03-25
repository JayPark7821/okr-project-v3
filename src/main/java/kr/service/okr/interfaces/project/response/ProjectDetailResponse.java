package kr.service.okr.interfaces.project.response;

import java.time.LocalDate;

public record ProjectDetailResponse(
	String objective, String projectToken, boolean newProject, double progress, LocalDate sdt, LocalDate edt, int teamMembersCount,
	String projectType
) {
}