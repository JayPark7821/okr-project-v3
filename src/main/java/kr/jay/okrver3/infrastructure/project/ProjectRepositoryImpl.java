package kr.jay.okrver3.infrastructure.project;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.project.ProjectRepository;
import kr.jay.okrver3.domain.project.command.ProjectDetailRetrieveCommand;
import kr.jay.okrver3.domain.user.User;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepository {

	private final ProjectJpaRepository projectJpaRepository;
	private final ProjectQueryDslRepository projectQueryDslRepository;

	@Override
	public Project save(Project project) {
		return projectJpaRepository.save(project);
	}

	@Override
	public Optional<Project> findByProjectTokenAndUser(String projectToken, User user) {
		return projectJpaRepository.findByProjectTokenAndUser(projectToken, user);
	}

	@Override
	public Optional<Project> findFetchedTeamMemberByProjectTokenAndUser(String projectToken, User inviter) {
		return projectJpaRepository.findFetchedTeamMemberByProjectTokenAndUser(projectToken, inviter);
	}

	@Override
	public Page<Project> getDetailProjectList(ProjectDetailRetrieveCommand command, User user) {
		return projectQueryDslRepository.getDetailProjectList(command, user);
	}

	@Override
	public Optional<Project> findProgressAndTeamMembersByProjectTokenAndUser(String projectToken, User user) {
		return projectJpaRepository.findProgressAndTeamMembersByProjectTokenAndUser(projectToken, user);
	}

	@Override
	public Optional<Project> findProjectKeyResultByProjectTokenAndUser(String projectToken, User user) {
		return projectJpaRepository.findProjectKeyResultByProjectTokenAndUser(projectToken, user);
	}

	@Override
	public Optional<Project> findByKeyResultTokenAndUser(String keyResultToken, User user) {
		return projectJpaRepository.findByKeyResultTokenAndUser(keyResultToken, user);
	}

	@Override
	public double getProjectProgress(Long projectId) {
		return projectQueryDslRepository.getProjectProgress(projectId);
	}

	@Override
	public Optional<Project> findProjectForUpdateById(Long projectId) {
		return projectJpaRepository.findProjectForUpdateById(projectId);
	}
}
