package kr.jay.okrver3.domain.project.service;

import java.util.Optional;

import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.user.User;

public interface ProjectRepository {
	Project save(Project project);

	Optional<Project> findByProjectTokenAndUser(String projectToken, User user);

	Optional<Project> findFetchedTeamMemberByProjectTokenAndUser(String projectToken, User inviter);
}
