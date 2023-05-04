package kr.service.okr.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.project.domain.Project;
import kr.service.okr.project.domain.TeamMember;
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
		final Project project = projectCommand.save(createProject(command));
		teamMemberCommand.saveAll(addProjectMembers(command, project));
		return project.getProjectToken();
	}

	private List<TeamMember> addProjectMembers(final Command command, final Project project) {
		project.createAndAddLeader(command.userSeq());
		if (command.teamMemberUsers() != null)
			addTeamMember(command, project);

		return project.getTeamMember();
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

		return new Project(command.objective(), command.startDate(), command.endDate());
	}

}
