package kr.jay.okrver3.infrastructure.project;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.project.ProjectType;
import kr.jay.okrver3.domain.project.SortType;
import kr.jay.okrver3.domain.project.service.ProjectDetailInfo;
import kr.jay.okrver3.domain.project.service.ProjectRepository;
import kr.jay.okrver3.domain.team.TeamMember;
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
	public Page<ProjectDetailInfo> getDetailProjectList(SortType sortType, ProjectType projectType,
		String validateIncludeFinishedProjectYN, User user, Pageable pageable) {

		return projectQueryDslRepository.getDetailProjectList(sortType, projectType,
			validateIncludeFinishedProjectYN,
			user, pageable).map(project -> getProjectDetailInfo(project, user.getEmail()));
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