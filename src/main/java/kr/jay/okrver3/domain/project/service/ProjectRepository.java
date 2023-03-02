package kr.jay.okrver3.domain.project.service;

import java.util.Optional;

import org.springframework.data.domain.Page;

import kr.jay.okrver3.application.project.ProjectDetailRetrieveCommand;
import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.user.User;

public interface ProjectRepository {
	Project save(Project project);

	Optional<Project> findByProjectTokenAndUser(String projectToken, User user);

	Optional<Project> findFetchedTeamMemberByProjectTokenAndUser(String projectToken, User inviter);

	Page<ProjectDetailInfo> getDetailProjectList(ProjectDetailRetrieveCommand command);

	Optional<Project> findProgressAndTeamMembersByProjectTokenAndUser(String projectToken, User user);

	Optional<Project> findProjectKeyResultByProjectTokenAndUser(String projectToken, User user);

	Optional<Project> findByKeyResultTokenAndUser(String keyResultToken, User user);
}
