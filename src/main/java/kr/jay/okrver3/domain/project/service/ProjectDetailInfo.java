package kr.jay.okrver3.domain.project.service;

import java.time.LocalDate;

import kr.jay.okrver3.domain.project.ProjectType;

public record ProjectDetailInfo(String projectToken, boolean newProject, double progress, LocalDate sdt, LocalDate edt, ProjectTeamMemberInfo teamMembers, ProjectType projectType) {
}
