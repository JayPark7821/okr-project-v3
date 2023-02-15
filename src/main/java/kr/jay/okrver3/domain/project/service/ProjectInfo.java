package kr.jay.okrver3.domain.project.service;

import kr.jay.okrver3.domain.project.Project;

public record ProjectInfo(String projectToken) {

	public ProjectInfo(Project project) {
		this(project.getProjectToken());
	}
}
