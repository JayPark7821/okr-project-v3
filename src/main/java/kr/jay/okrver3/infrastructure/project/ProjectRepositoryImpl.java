package kr.jay.okrver3.infrastructure.project;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.project.ProjectRepository;
import kr.jay.okrver3.domain.project.command.ProjectDetailRetrieveCommand;
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
	public Optional<Project> findByProjectTokenAndUser(String projectToken, Long userSeq) {
		return projectJpaRepository.findByProjectTokenAndUser(projectToken, userSeq);
	}

	@Override
	public Optional<Project> findFetchedTeamMemberByProjectTokenAndUser(String projectToken, Long inviterSeq) {
		return projectJpaRepository.findFetchedTeamMemberByProjectTokenAndUser(projectToken, inviterSeq);
	}

	@Override
	public Page<Project> getDetailProjectList(ProjectDetailRetrieveCommand command, Long userSeq) {
		return projectQueryDslRepository.getDetailProjectList(command, userSeq);
	}

	@Override
	public Optional<Project> findProgressAndTeamMembersByProjectTokenAndUser(String projectToken, Long userSeq) {
		return projectJpaRepository.findProgressAndTeamMembersByProjectTokenAndUser(projectToken, userSeq);
	}

	@Override
	public Optional<Project> findProjectKeyResultByProjectTokenAndUser(String projectToken, Long userSeq) {
		return projectJpaRepository.findProjectKeyResultByProjectTokenAndUser(projectToken, userSeq);
	}

	@Override
	public Optional<Project> findByKeyResultTokenAndUser(String keyResultToken, Long userSeq) {
		return projectJpaRepository.findByKeyResultTokenAndUser(keyResultToken, userSeq);
	}

	@Override
	public double getProjectProgress(Long projectId) {
		return projectQueryDslRepository.getProjectProgress(projectId);
	}

	@Override
	public Optional<Project> findProjectForUpdateById(Long projectId) {
		return projectJpaRepository.findProjectForUpdateById(projectId);
	}

	@Override
	public List<Project> findParticipateProjectByUserSeq(final Long userSeq) {
		return projectJpaRepository.findParticipateProjectByUserSeq(userSeq);
	}

	@Override
	public void saveAndFlush(final Project project) {
		projectJpaRepository.saveAndFlush(project);
	}
}
