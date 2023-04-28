package kr.service.okr.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.project.domain.Project;
import kr.service.okr.project.repository.ProjectCommand;
import kr.service.okr.project.repository.ProjectQuery;
import kr.service.okr.project.usecase.RegisterKeyResultUseCase;
import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class RegisterKeyResult implements RegisterKeyResultUseCase {

	private final ProjectQuery projectQuery;
	private final ProjectCommand projectCommand;

	@Override
	public String command(final Command command) {
		Project project =
			projectQuery.findProjectKeyResultByProjectTokenAndUser(command.projectToken(), command.requesterSeq())
				.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN));

		project.addKeyResult(command.keyResultName(), command.requesterSeq());

		return projectCommand.save(project).getProjectToken();
	}
}
