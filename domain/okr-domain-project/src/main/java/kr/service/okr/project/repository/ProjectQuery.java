package kr.service.okr.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.service.okr.project.domain.Project;
import kr.service.okr.project.domain.enums.ProjectType;
import kr.service.okr.project.domain.enums.SortType;

public interface ProjectQuery {
	Optional<Project> findByProjectTokenAndUser(String projectToken, Long userSeq);

	Optional<Project> findFetchedTeamMemberByProjectTokenAndUser(String projectToken, Long inviterSeq);

	Optional<Project> findProjectForRegisterKeyResult(String projectToken, Long userSeq);

	Optional<Project> findProjectForUpdateKeyResult(String keyResultToken, Long requesterSeq);

	Page<Project> getDetailProjectList(
		SortType sortType,
		ProjectType projectType,
		String includeFinishedProjectYN,
		Pageable pageable,
		Long userSeq
	);

	Optional<Project> findProgressAndTeamMembersByProjectTokenAndUser(String projectToken, Long userSeq);

	Optional<Project> findByKeyResultTokenAndUser(String keyResultToken, Long userSeq);

	double getProjectProgress(Long projectId);

	Optional<Project> findProjectForUpdateById(Long projectId);

	List<Project> findParticipateProjectByUserSeq(Long userSeq);

}
