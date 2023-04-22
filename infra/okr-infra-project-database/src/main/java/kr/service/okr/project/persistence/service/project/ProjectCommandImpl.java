package kr.service.okr.project.persistence.service.project;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.project.domain.Project;
import kr.service.okr.project.persistence.entity.project.ProjectJpaEntity;
import kr.service.okr.project.persistence.repository.project.ProjectJpaRepository;
import kr.service.okr.project.persistence.repository.project.ProjectQueryDslRepository;
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
		if (project.getId() == null) {
			final ProjectJpaEntity savedProject = projectJpaRepository.save(ProjectJpaEntity.createFrom(project));
			project.getTeamMember().forEach(savedProject::addTeamMember);
			return savedProject.toDomain();
		} else {
			return projectJpaRepository.save(new ProjectJpaEntity(project)).toDomain();
		}
	}

	private void create(final Project project) {

	}

	@Override
	public void delete(final Project project) {

	}
}
