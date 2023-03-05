package kr.jay.okrver3.domain.project.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

import kr.jay.okrver3.domain.initiative.Initiative;
import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.application.project.ProjectDetailRetrieveCommand;

public interface ProjectRepository {
	Project save(Project project);

	Optional<Project> findByProjectTokenAndUser(String projectToken, User user);

	Optional<Project> findFetchedTeamMemberByProjectTokenAndUser(String projectToken, User inviter);

	Page<ProjectDetailInfo> getDetailProjectList(ProjectDetailRetrieveCommand command);

	Optional<Project> findProgressAndTeamMembersByProjectTokenAndUser(String projectToken, User user);

	Optional<Project> findProjectKeyResultByProjectTokenAndUser(String projectToken, User user);

	Optional<Project> findByKeyResultTokenAndUser(String keyResultToken, User user);

	double getProjectProgress(Long projectId);

	Optional<Initiative> findProjectInitiativeByInitiativeTokenAndUser(String initiativeToken, User user);

	Project getReferenceById(Long projectId);

	Optional<Project> findProjectForUpdateById(Long projectId);
}
