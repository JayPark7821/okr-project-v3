package kr.service.okr.persistence.service.project;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.persistence.entity.project.ProjectJpaEntity;
import kr.service.okr.persistence.repository.project.ProjectJpaRepository;
import kr.service.okr.persistence.repository.project.ProjectQueryDslRepository;
import kr.service.okr.project.domain.Project;
import kr.service.okr.project.repository.ProjectCommand;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
public class ProjectCommandImpl implements ProjectCommand {

	private final ProjectJpaRepository projectJpaRepository;
	private final ProjectQueryDslRepository projectQueryDslRepository;

	@Override
	public Project save(final Project project) {
		final ProjectJpaEntity projectJpaEntity = projectJpaRepository.save(new ProjectJpaEntity(project));
		return null;
	}

	@Override
	public void delete(final Project project) {

	}
}
