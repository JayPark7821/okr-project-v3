package kr.jay.okrver3.infrastructure.project;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import kr.jay.okrver3.application.project.ProjectDetailRetrieveCommand;
import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.initiative.Initiative;
import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.project.service.ProjectDetailInfo;
import kr.jay.okrver3.domain.project.service.ProjectRepository;
import kr.jay.okrver3.domain.team.TeamMember;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.infrastructure.initiative.InitiativeJpaRepository;
import kr.jay.okrver3.infrastructure.initiative.InitiativeQueryDslRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepository {

	private final ProjectJpaRepository projectJpaRepository;
	private final InitiativeJpaRepository initiativeJpaRepository;
	private final ProjectQueryDslRepository projectQueryDslRepository;
	private final InitiativeQueryDslRepository initiativeQueryDslRepository;

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
	public Page<ProjectDetailInfo> getDetailProjectList(ProjectDetailRetrieveCommand command) {

		return projectQueryDslRepository.getDetailProjectList(command)
			.map(project -> getProjectDetailInfo(project, command.user().getEmail()));
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
	public Optional<Initiative> findProjectInitiativeByInitiativeTokenAndUser(String initiativeToken, User user) {
		return initiativeJpaRepository.findProjectInitiativeByInitiativeTokenAndUser(initiativeToken, user);
	}

	@Override
	public Project getReferenceById(Long projectId) {
		return projectJpaRepository.getReferenceById(projectId);

	}

	@Override
	public Optional<Project> findProjectForUpdateById(Long projectId) {
		return projectJpaRepository.findProjectForUpdateById(projectId);
	}

	@Override
	public Page<Initiative> findInitiativeByKeyResultTokenAndUser(
		String keyResultToken,
		User user,
		Pageable pageable
	) {
		return initiativeQueryDslRepository.findInitiativeByKeyResultTokenAndUser(keyResultToken, user, pageable);
	}

	private ProjectDetailInfo getProjectDetailInfo(Project project, String email) {
		return new ProjectDetailInfo(
			project.getProjectToken(),
			project.getTeamMember().stream()
				.filter(t -> t.getUser().getEmail().equals(email))
				.findFirst()
				.map(TeamMember::isNew)
				.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_USER_INFO)),
			project.getProgress(),
			project.getStartDate(),
			project.getEndDate(),
			project.getTeamMember().size(),
			project.getType().name());

	}
}
