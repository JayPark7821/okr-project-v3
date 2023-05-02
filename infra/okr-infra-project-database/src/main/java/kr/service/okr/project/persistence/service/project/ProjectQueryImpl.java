package kr.service.okr.project.persistence.service.project;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.project.domain.Project;
import kr.service.okr.project.persistence.entity.project.ProjectJpaEntity;
import kr.service.okr.project.persistence.repository.project.ProjectJpaRepository;
import kr.service.okr.project.persistence.repository.project.ProjectQueryDslRepository;
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
		return projectJpaRepository.findByProjectTokenAndUser(
			projectToken, userSeq).map(ProjectJpaEntity::toDomain);

	}

	@Override
	public Optional<Project> findProjectForUpdateKeyResult(final String keyResultToken, final Long requesterSeq) {
		return projectJpaRepository.findByKeyResultTokenAndUser(keyResultToken, requesterSeq)
			.map(ProjectJpaEntity::toDomain);
	}

	@Override
	public Optional<Project> findProjectForRegisterInitiative(final String keyResultToken, final Long requesterSeq) {
		return projectJpaRepository.findInitiativeByKeyResultTokenAndUser(keyResultToken, requesterSeq)
			.map(ProjectJpaEntity::toDomain);
	}

	@Override
	public Optional<Project> findProjectForRegisterKeyResult(final String projectToken, final Long userSeq) {
		return projectJpaRepository.findProjectForRegisterKeyResultByProjectTokenAndUser(projectToken, userSeq)
			.map(ProjectJpaEntity::toDomain);
	}

	@Override
	public Optional<Project> findFetchedTeamMemberByProjectTokenAndUser(final String projectToken,
		final Long inviterSeq) {
		return Optional.empty();
	}

}
