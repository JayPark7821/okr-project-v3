package kr.jay.okrver3.domain.project.service.impl;

import org.springframework.stereotype.Service;

import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.project.service.ProjectInfo;
import kr.jay.okrver3.domain.project.service.ProjectRepository;
import kr.jay.okrver3.domain.project.service.ProjectService;
import kr.jay.okrver3.domain.team.TeamMember;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.interfaces.project.ProjectMasterSaveDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;

	@Override
	public ProjectInfo registerProject(ProjectMasterSaveDto dto) {
		return new ProjectInfo(projectRepository.save(dto.toEntity()));
	}

	@Override
	public ProjectInfo getProjectInfoBy(String projectToken, User user) {
		return projectRepository.findByProjectTokenAndUser(projectToken, user)
			.map(ProjectInfo::new)
			.orElseThrow(() -> new IllegalArgumentException("해당 프로젝트가 존재하지 않습니다."));
	}

	@Override
	public String inviteTeamMember(String projectToken, User invitedUser,
		User inviter) {

		Project project = projectRepository.findFetchedTeamMemberByProjectTokenAndUser(projectToken, inviter)
			.orElseThrow(() -> new IllegalArgumentException("해당 프로젝트가 존재하지 않습니다."));

		project.inviteTeamMember(
			TeamMember.builder()
				.project(project)
				.user(invitedUser)
				.build()
		);

		return invitedUser.getEmail();
	}
}
