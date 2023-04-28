package kr.service.okr.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.project.domain.Project;
import kr.service.okr.project.repository.ProjectQuery;
import kr.service.okr.project.usecase.ProjectInfo;
import kr.service.okr.project.usecase.QueryProjectInfoUseCase;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class QueryProjectInfo implements QueryProjectInfoUseCase {

	private final ProjectQuery projectQuery;

	@Override
	public ProjectInfo query(final Query query) {
		final Project project = projectQuery.findByProjectTokenAndUser(query.projectToken(), query.userSeq())
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.PROJECT_NOT_FOUND));

		setNewProjectToOldOneForRequester(query.userSeq(), project);

		return new ProjectInfo(project, query.userSeq());
	}

	private void setNewProjectToOldOneForRequester(final Long userSeq, final Project project) {
		project.getTeamMember()
			.stream()
			.filter(teamMember -> teamMember.getUserSeq().equals(userSeq))
			.findFirst()
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INTERNAL_SERVER_ERROR))
			.deleteNewProjectMark();
	}
}
