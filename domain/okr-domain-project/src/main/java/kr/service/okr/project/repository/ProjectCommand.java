package kr.service.okr.project.repository;

import kr.service.okr.project.domain.Project;

public interface ProjectCommand {
	Project save(Project project);

	void delete(Project project);
}
