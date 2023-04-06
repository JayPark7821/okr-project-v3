package kr.service.okr.persistence.service.project;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.model.project.ProjectType;
import kr.service.okr.model.project.SortType;
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
	public Optional<Project> findByProjectTokenAndUser(final String projectToken, final Long userSeq) {
		return Optional.empty();
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
	public Optional<Project> findProjectKeyResultByProjectTokenAndUser(final String projectToken, final Long userSeq) {
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
