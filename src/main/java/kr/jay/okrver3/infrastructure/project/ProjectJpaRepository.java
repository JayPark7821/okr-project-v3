package kr.jay.okrver3.infrastructure.project;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.project.service.ProjectRepository;

public interface ProjectJpaRepository extends ProjectRepository, JpaRepository<Project, Long> {

}
