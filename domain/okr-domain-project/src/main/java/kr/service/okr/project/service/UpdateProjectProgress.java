package kr.service.okr.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.project.domain.Project;
import kr.service.okr.project.repository.ProjectCommand;
import kr.service.okr.project.repository.ProjectQuery;
import kr.service.okr.project.usecase.UpdateProjectProgressUseCase;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateProjectProgress implements UpdateProjectProgressUseCase {

	private final ProjectQuery projectQuery;
	private final ProjectCommand projectCommand;

	@Override
	public void command(final Long projectId) {
		final Project project = projectQuery.findProjectById(projectId)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.PROJECT_NOT_FOUND));
		project.updateProgress(projectQuery.getProjectProgress(projectId));
		projectCommand.save(project);
	}
}
