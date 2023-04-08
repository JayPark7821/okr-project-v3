package kr.service.okr.project.usecase;

import kr.service.okr.project.domain.Project;

public interface InviteTeamMemberToProjectUseCase {

	Project inviteTeamMemberToProject(Command command);

	record Command(
		String projectToken,
		Long invitedUserSeq,
		Long inviterSeq
	) {

	}
}
