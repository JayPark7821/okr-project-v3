package kr.service.okr.project.repository;

import java.util.HashMap;
import java.util.Map;

import kr.service.okr.project.domain.Project;

public class TestProjectRepository {

	public static Map<Long, Project> persistence = new HashMap<>();
	private static Long sequence = 0L;

	public static void clear() {
		persistence.clear();
		sequence = 0L;
	}

	public static class TestProjectCommand implements ProjectCommand {

		@Override
		public Project save(Project project) {
			if (project.getId() == null) {
				project.assignId(++sequence);
			}
			persistence.put(project.getId(), project);
			return project;
		}

		@Override
		public void delete(Project project) {
			persistence.remove(project.getId());
		}
	}
}
