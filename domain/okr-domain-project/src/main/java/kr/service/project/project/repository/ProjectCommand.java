package kr.service.project.project.repository;

import kr.service.project.project.domain.Project;

public interface ProjectCommand {
	Project save(Project project);

	void delete(Project project);
}
