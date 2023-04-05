package kr.service.okr.persistence.service.project;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.persistence.repository.project.ProjectJpaRepository;
import kr.service.okr.persistence.repository.project.ProjectQueryDslRepository;
import kr.service.okr.project.domain.Project;
import kr.service.okr.project.repository.ProjectQuery;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectQueryImpl implements ProjectQuery {

	private final ProjectJpaRepository projectJpaRepository;
	private final ProjectQueryDslRepository projectQueryDslRepository;

	@Override
	public Optional<Project> findByProjectTokenAndUser(final QueryProject query) {
		return Optional.empty();
	}

	@Override
	public Optional<Project> findFetchedTeamMemberByProjectTokenAndUser(final QueryProject query) {
		return Optional.empty();
	}

	@Override
	public Optional<Project> findProgressAndTeamMembersByProjectTokenAndUser(final QueryProject query) {
		return Optional.empty();
	}

	@Override
	public Optional<Project> findProjectKeyResultByProjectTokenAndUser(final QueryProject query) {
		return Optional.empty();
	}

	@Override
	public Page<Project> getDetailProjectList(final QueryDetailProjectList query) {
		return null;
	}

	@Override
	public Optional<Project> findByKeyResultTokenAndUser(final QueryProject query) {
		return Optional.empty();
	}

	@Override
	public double getProjectProgress(final Query projectId) {
		return 0;
	}

	@Override
	public Optional<Project> findProjectForUpdateById(final Query projectId) {
		return Optional.empty();
	}

	@Override
	public List<Project> findParticipateProjectByUserSeq(final Query userSeq) {
		return null;
	}
}
