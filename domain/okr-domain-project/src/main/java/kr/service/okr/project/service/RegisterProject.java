package kr.service.okr.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.exception.ErrorCode;
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

		assertLeaderIsNotInTeamMember(command);

		final Project project = new Project(command.objective(), command.startDate(), command.endDate());
		project.addLeader(command.userSeq());
		command.teamMemberUserSeqs().forEach(project::addTeamMember);

		return projectCommand.save(project).getProjectToken();
	}

	private void assertLeaderIsNotInTeamMember(final Command command) {
		if (command.teamMemberUserSeqs().stream().anyMatch(teamMember -> teamMember.equals(command.userSeq())))
			throw new IllegalArgumentException(ErrorCode.LEADER_IS_IN_TEAM_MEMBER.getMessage());
	}
}
