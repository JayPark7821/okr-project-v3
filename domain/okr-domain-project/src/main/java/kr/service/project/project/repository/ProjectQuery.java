package kr.service.project.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.service.model.project.ProjectType;
import kr.service.model.project.SortType;
import kr.service.project.project.domain.Project;

public interface ProjectQuery {
	Optional<Project> findByProjectTokenAndUser(QueryProject query);

	Optional<Project> findFetchedTeamMemberByProjectTokenAndUser(QueryProject query);

	Optional<Project> findProgressAndTeamMembersByProjectTokenAndUser(QueryProject query);

	Optional<Project> findProjectKeyResultByProjectTokenAndUser(QueryProject query);

	Page<Project> getDetailProjectList(QueryDetailProjectList query);

	Optional<Project> findByKeyResultTokenAndUser(QueryProject query);

	double getProjectProgress(Query projectId);

	Optional<Project> findProjectForUpdateById(Query projectId);

	List<Project> findParticipateProjectByUserSeq(Query userSeq);

	record Query(
		long id
	) {
	}

	record QueryProject(
		String Token,
		Long userSeq
	) {
	}

	record QueryDetailProjectList(
		SortType sortType,
		ProjectType projectType,
		String includeFinishedProjectYN,
		Long userSeq,
		Pageable pageable
	) {
	}

}
