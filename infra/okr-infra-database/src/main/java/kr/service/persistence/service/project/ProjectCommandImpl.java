package kr.service.persistence.service.project;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.persistence.entity.project.ProjectJpaEntity;
import kr.service.persistence.repository.project.ProjectJpaRepository;
import kr.service.persistence.repository.project.ProjectQueryDslRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectCommandImpl {

	private final ProjectJpaRepository projectJpaRepository;
	private final ProjectQueryDslRepository projectQueryDslRepository;

	public ProjectJpaEntity save(ProjectJpaEntity project) {
		return projectJpaRepository.save(project);
	}

	public void delete(final ProjectJpaEntity project) {
		projectJpaRepository.delete(project);
	}
}
