package kr.service.okr.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public String registerProject(Command command) {

		final Project project = createProject(command);
		if (command.teamMemberUserSeqs() != null)
			addTeamMember(command, project);

		return projectCommand.save(project).getProjectToken();
	}

	private void addTeamMember(final Command command, final Project project) {
		command.teamMemberUserSeqs()
			.forEach(teamMemberSeq -> project.createAndAddMemberOf(teamMemberSeq, command.userSeq()));
	}

	private Project createProject(final Command command) {
		final Project project = new Project(command.objective(), command.startDate(), command.endDate());
		project.createAndAddLeader(command.userSeq());
		return project;
	}

}
