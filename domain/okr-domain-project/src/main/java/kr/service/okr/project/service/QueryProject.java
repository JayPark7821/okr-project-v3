package kr.service.okr.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.project.domain.Project;
import kr.service.okr.project.domain.TeamMember;
import kr.service.okr.project.repository.ProjectQuery;
import kr.service.okr.project.usecase.QueryProjectUseCase;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class QueryProject implements QueryProjectUseCase {

	private final ProjectQuery projectQuery;

	@Override
	public Project findProjectBy(final QueryProjectBy query) {
		final Project project = projectQuery.findByProjectTokenAndUser(query.projectToken(), query.userSeq())
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.PROJECT_NOT_FOUND));

		getRequestedTeamMemberFromProject(query.userSeq(), project).deleteNewProjectMark();

		return project;
	}

	private TeamMember getRequestedTeamMemberFromProject(final Long userSeq, final Project project) {
		return project.getTeamMember()
			.stream()
			.filter(teamMember -> teamMember.getUserSeq().equals(userSeq))
			.findFirst()
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INTERNAL_SERVER_ERROR));
	}
}
