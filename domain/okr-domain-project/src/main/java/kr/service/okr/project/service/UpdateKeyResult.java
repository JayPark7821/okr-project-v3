package kr.service.okr.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.project.domain.Project;
import kr.service.okr.project.repository.KeyResultCommand;
import kr.service.okr.project.repository.ProjectQuery;
import kr.service.okr.project.usecase.UpdateKeyResultUseCase;
import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class UpdateKeyResult implements UpdateKeyResultUseCase {

	private final ProjectQuery projectQuery;
	private final KeyResultCommand keyResultCommand;

	@Override
	public void command(final Command command) {
		Project project =
			projectQuery.findProjectForUpdateKeyResult(command.keyResultToken(), command.requesterSeq())
				.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN));

		keyResultCommand.save(
			project.updateKeyResult(command.keyResultToken(), command.keyResultName(), command.requesterSeq())
		);
	}
}
