package kr.jay.okrver3.domain.project.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.project.ProjectType;
import kr.jay.okrver3.domain.project.SortType;
import kr.jay.okrver3.domain.user.User;

public interface ProjectRepository {
	Project save(Project project);

	Optional<Project> findByProjectTokenAndUser(String projectToken, User user);

	Optional<Project> findFetchedTeamMemberByProjectTokenAndUser(String projectToken, User inviter);

	Page<ProjectDetailInfo> getDetailProjectList(SortType sortType, ProjectType projectType,
		String validateIncludeFinishedProjectYN, User user, Pageable pageable);
}
