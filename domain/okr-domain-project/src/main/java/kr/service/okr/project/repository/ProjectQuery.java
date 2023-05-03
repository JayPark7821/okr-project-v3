package kr.service.okr.project.repository;

import java.util.Optional;

import kr.service.okr.project.domain.Project;

public interface ProjectQuery {
	Optional<Project> findByProjectTokenAndUser(String projectToken, Long userSeq);

	Optional<Project> findFetchedTeamMemberByProjectTokenAndUser(String projectToken, Long inviterSeq);

	Optional<Project> findProjectForRegisterKeyResult(String projectToken, Long userSeq);

	Optional<Project> findProjectForUpdateKeyResult(String keyResultToken, Long requesterSeq);

	Optional<Project> findProjectForRegisterInitiative(String keyResultToken, Long requesterSeq);

	double getProjectProgress(Long projectId);

	Optional<Project> findProjectById(Long projectId);
}
