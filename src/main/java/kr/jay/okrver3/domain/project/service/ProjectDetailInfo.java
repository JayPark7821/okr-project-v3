package kr.jay.okrver3.domain.project.service;

import java.time.LocalDate;

public record ProjectDetailInfo(String projectToken, boolean newProject, double progress, LocalDate sdt, LocalDate edt,
								int teamMemberCount, String projectType) {

}
