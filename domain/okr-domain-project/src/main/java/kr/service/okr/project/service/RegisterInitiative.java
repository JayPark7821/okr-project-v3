package kr.service.okr.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.project.domain.Initiative;
import kr.service.okr.project.domain.Project;
import kr.service.okr.project.repository.InitiativeCommand;
import kr.service.okr.project.repository.ProjectQuery;
import kr.service.okr.project.usecase.RegisterInitiativeInfo;
import kr.service.okr.project.usecase.RegisterInitiativeUseCase;
import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor
public class RegisterInitiative implements RegisterInitiativeUseCase {

	private final ProjectQuery projectQuery;
	private final InitiativeCommand initiativeCommand;

	@Override
	public RegisterInitiativeInfo command(final Command command) {
		final Project project = projectQuery.findProjectForRegisterInitiative(
				command.keyResultToken(), command.requesterSeq())
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN));

		final Initiative initiative = project.addInitiative(command.keyResultToken(), command.name(), command.detail(),
			command.startDate(), command.endDate(), command.requesterSeq());

		return new RegisterInitiativeInfo(initiativeCommand.save(initiative).getInitiativeToken(), project.getId());
	}
	
}
