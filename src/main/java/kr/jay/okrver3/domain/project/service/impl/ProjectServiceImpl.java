package kr.jay.okrver3.domain.project.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.project.service.ProjectInfo;
import kr.jay.okrver3.domain.project.service.ProjectRepository;
import kr.jay.okrver3.domain.project.service.ProjectService;
import kr.jay.okrver3.domain.project.service.ProjectTeamMemberInfo;
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

	@Transactional
	@Override
	public ProjectInfo registerProject(ProjectMasterSaveDto dto, User user, List<User> teamMemberUsers) {
		Project project = projectRepository.save(buildProjectFrom(dto));
		project.addLeader(user);
		teamMemberUsers.forEach(project::addTeamMember);
		return new ProjectInfo(project);
	}

	private Project buildProjectFrom(ProjectMasterSaveDto dto) {
		LocalDate startDt = LocalDate.parse(dto.sdt(), DateTimeFormatter.ofPattern("yyyyMMdd"));
		LocalDate endDt = LocalDate.parse(dto.edt(), DateTimeFormatter.ofPattern("yyyyMMdd"));

		return Project.builder()
			.name(dto.name())
			.startDate(startDt)
			.endDate(endDt)
			.objective(dto.objective())
			.progress(0)
			.keyResultList(dto.keyResults())
			.build();
	}

	@Override
	public ProjectInfo getProjectInfoBy(String projectToken, User user) {
		return projectRepository.findByProjectTokenAndUser(projectToken, user)
			.map(ProjectInfo::new)
			.orElseThrow(() -> new IllegalArgumentException("해당 프로젝트가 존재하지 않습니다."));
	}

	@Override
	public ProjectTeamMemberInfo inviteTeamMember(String projectToken, User invitedUser, User inviter) {

		Project project = projectRepository.findFetchedTeamMemberByProjectTokenAndUser(projectToken, inviter)
			.orElseThrow(() -> new IllegalArgumentException("해당 프로젝트가 존재하지 않습니다."));

		project.addTeamMember(invitedUser);
		return new ProjectTeamMemberInfo(project.getTeamMember().stream().map(TeamMember::getUser).toList(),project.getName());
	}

	@Override
	public String validateEmail(String projectToken, String email, User user) {
		return null;
	}
}
