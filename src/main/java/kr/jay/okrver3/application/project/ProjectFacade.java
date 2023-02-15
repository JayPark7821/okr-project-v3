package kr.jay.okrver3.application.project;

import org.springframework.stereotype.Service;

import kr.jay.okrver3.domain.project.service.ProjectInfo;
import kr.jay.okrver3.domain.project.service.ProjectService;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.interfaces.project.ProjectMasterSaveDto;
import kr.jay.okrver3.interfaces.project.TeamMemberInviteRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectFacade {

	private final ProjectService projectService;

	public String registerProject(ProjectMasterSaveDto dto, User user) {
		ProjectInfo projectInfo = projectService.registerProject(dto);
		return projectInfo.projectToken();
	}

	public ProjectInfo getProjectInfoBy(String projectToken, User user) {
		return projectService.getProjectInfoBy(projectToken, user);
	}

	public String inviteTeamMember(TeamMemberInviteRequestDto teamMemberInviteRequestDto, User user) {
		return projectService.inviteTeamMember(teamMemberInviteRequestDto, user);
	}
}
