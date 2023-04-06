package kr.service.okr.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.project.aggregate.team.domain.TeamMember;
import kr.service.okr.project.domain.Project;
import kr.service.okr.project.repository.ProjectQuery;
import kr.service.okr.project.usecase.QueryProjectByProjectTokenAndUserSeqUseCase;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class QueryProjectByProjectTokenAndUserSeq implements QueryProjectByProjectTokenAndUserSeqUseCase {

	private final ProjectQuery projectQuery;

	@Override
	public Project findProjectBy(final Query query) {
		final Project project = projectQuery.findByProjectTokenAndUser(query.projectToken(), query.userSeq())
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.PROJECT_NOT_FOUND));

		getRequestedTeamMemberFromProject(query, project).deleteNewProjectMark();

		return project;
	}

	private static TeamMember getRequestedTeamMemberFromProject(final Query query, final Project project) {
		return project.getTeamMember().stream().filter(teamMember -> teamMember.getUserSeq().equals(query.userSeq()))
			.findFirst()
			.orElseThrow(RuntimeException::new);
	}
}
