package kr.service.okr.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrProjectDomainException;
import kr.service.okr.project.domain.Project;
import kr.service.okr.project.repository.ProjectCommand;
import kr.service.okr.project.repository.ProjectQuery;
import kr.service.okr.project.usecase.InviteTeamMemberToProjectUseCase;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class InviteTeamMemberToProject implements InviteTeamMemberToProjectUseCase {

	private final ProjectCommand projectCommand;
	private final ProjectQuery projectQuery;

	@Override
	public Project inviteTeamMemberToProject(final Command command) {
		Project project = projectQuery.findFetchedTeamMemberByProjectTokenAndUser(command.projectToken(),
				command.inviterSeq())
			.orElseThrow(() -> new OkrProjectDomainException(ErrorCode.INVALID_PROJECT_TOKEN));

		project.createAndAddMemberOf(command.invitedUserSeq(), command.inviterSeq());

		return projectCommand.save(project);
	}
}
