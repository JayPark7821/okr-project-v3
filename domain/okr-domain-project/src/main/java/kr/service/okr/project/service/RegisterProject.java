package kr.service.okr.project.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.project.domain.Project;
import kr.service.okr.project.repository.ProjectCommand;
import kr.service.okr.project.repository.TeamMemberCommand;
import kr.service.okr.project.usecase.RegisterProjectUseCase;
import kr.service.okr.user.repository.user.UserQuery;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterProject implements RegisterProjectUseCase {

	private final ProjectCommand projectCommand;
	private final TeamMemberCommand teamMemberCommand;
	private final UserQuery userQuery;

	@Override
	public String command(Command command) {

		final Project project = createProject(command);

		if (command.teamMemberUsers() != null)
			addTeamMember(command, project);
		final Project savedProject = projectCommand.save(project);

		teamMemberCommand.saveAll(savedProject.getTeamMember());
		return savedProject.getProjectToken();
	}

	private void addTeamMember(final Command command, final Project project) {
		command.teamMemberUsers()
			.stream()
			.map(userQuery::findByEmail)
			.filter(Optional::isPresent)
			.map(user -> user.get().getUserSeq())
			.forEach(seq -> project.createAndAddMemberOf(seq, command.userSeq()));

	}

	private Project createProject(final Command command) {
		final Project project = new Project(command.objective(), command.startDate(), command.endDate());
		project.createAndAddLeader(command.userSeq());
		return project;
	}

}
