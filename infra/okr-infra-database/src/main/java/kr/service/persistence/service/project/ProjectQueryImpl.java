package kr.service.persistence.service.project;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.model.project.ProjectType;
import kr.service.model.project.SortType;
import kr.service.persistence.entity.project.ProjectJpaEntity;
import kr.service.persistence.repository.project.ProjectJpaRepository;
import kr.service.persistence.repository.project.ProjectQueryDslRepository;
import kr.service.project.project.domain.Project;
import kr.service.project.project.repository.ProjectQuery;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectQueryImpl implements ProjectQuery {

	private final ProjectJpaRepository projectJpaRepository;
	private final ProjectQueryDslRepository projectQueryDslRepository;

	@Override
	public Optional<Project> findByProjectTokenAndUser(QueryProject query) {
		return projectJpaRepository.findByProjectTokenAndUser(query.Token(), query.userSeq())
			.map(ProjectJpaEntity::toDomain);
	}

	@Override
	public Optional<ProjectJpaEntity> findFetchedTeamMemberByProjectTokenAndUser(String projectToken, Long inviterSeq) {
		return projectJpaRepository.findFetchedTeamMemberByProjectTokenAndUser(projectToken, inviterSeq);
	}

	@Override
	public Page<ProjectJpaEntity> getDetailProjectList(
		SortType sortType,
		ProjectType projectType,
		String includeFinishedProjectYN,
		Long userSeq,
		Pageable pageable
	) {
		return projectQueryDslRepository.getDetailProjectList(sortType, projectType, includeFinishedProjectYN, userSeq,
			pageable);
	}

	@Override
	public Optional<ProjectJpaEntity> findProgressAndTeamMembersByProjectTokenAndUser(QueryProject query) {
		return projectJpaRepository.findProgressAndTeamMembersByProjectTokenAndUser(query.Token(), query.userSeq());
	}

	@Override
	public Optional<ProjectJpaEntity> findProjectKeyResultByProjectTokenAndUser(QueryProject query) {
		return projectJpaRepository.findProjectKeyResultByProjectTokenAndUser(query.Token(), query.userSeq());
	}

	@Override
	public Optional<ProjectJpaEntity> findByKeyResultTokenAndUser(QueryProject query) {
		return projectJpaRepository.findByKeyResultTokenAndUser(query.Token(), query.userSeq());
	}

	@Override
	public double getProjectProgress(Long projectId) {
		return projectQueryDslRepository.getProjectProgress(projectId);
	}

	@Override
	public Optional<ProjectJpaEntity> findProjectForUpdateById(Long projectId) {
		return projectJpaRepository.findProjectForUpdateById(projectId);
	}

	@Override
	public List<ProjectJpaEntity> findParticipateProjectByUserSeq(final Long userSeq) {
		return projectJpaRepository.findParticipateProjectByUserSeq(userSeq);
	}

}
