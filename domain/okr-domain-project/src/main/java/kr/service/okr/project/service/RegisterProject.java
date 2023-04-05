package kr.service.okr.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.project.aggregate.team.domain.TeamMember;
import kr.service.okr.project.domain.Project;
import kr.service.okr.project.repository.ProjectCommand;
import kr.service.okr.project.usecase.RegisterProjectUseCase;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterProject implements RegisterProjectUseCase {

	private final ProjectCommand projectCommand;

	@Override
	public Project registerProject(Command command) {
		final Project project = new Project(command.objective(), command.startDate(), command.endDate());
		project.addLeader(TeamMember.createLeader(command.userSeq()));
		command.teamMemberUserSeqs().forEach(teamMember -> project.addTeamMember(TeamMember.createMember(teamMember)));

		return projectCommand.save(project);
	}
}
