package kr.service.okr.project.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.service.okr.model.project.ProjectType;
import kr.service.okr.model.project.SortType;
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

	public static class TestProjectQuery implements ProjectQuery {

		@Override
		public Optional<Project> findByProjectTokenAndUser(final String projectToken, final Long userSeq) {
			return Optional.of(persistence.get(0L));
		}

		@Override
		public Optional<Project> findFetchedTeamMemberByProjectTokenAndUser(final String projectToken,
			final Long inviterSeq) {
			return Optional.empty();
		}

		@Override
		public Page<Project> getDetailProjectList(final SortType sortType, final ProjectType projectType,
			final String includeFinishedProjectYN, final Pageable pageable, final Long userSeq) {
			return null;
		}

		@Override
		public Optional<Project> findProgressAndTeamMembersByProjectTokenAndUser(final String projectToken,
			final Long userSeq) {
			return Optional.empty();
		}

		@Override
		public Optional<Project> findProjectKeyResultByProjectTokenAndUser(final String projectToken,
			final Long userSeq) {
			return Optional.empty();
		}

		@Override
		public Optional<Project> findByKeyResultTokenAndUser(final String keyResultToken, final Long userSeq) {
			return Optional.empty();
		}

		@Override
		public double getProjectProgress(final Long projectId) {
			return 0;
		}

		@Override
		public Optional<Project> findProjectForUpdateById(final Long projectId) {
			return Optional.empty();
		}

		@Override
		public List<Project> findParticipateProjectByUserSeq(final Long userSeq) {
			return null;
		}
	}
}
