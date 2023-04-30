package kr.service.okr.api.project;

import kr.service.okr.application.project.RegisterKeyResultCommand;
import kr.service.okr.application.project.UpdateKeyResultCommand;
import kr.service.okr.project.api.RegisterKeyResultRequest;
import kr.service.okr.project.api.UpdateKeyResultRequest;

public class KeyResultDtoMapper {

	static RegisterKeyResultCommand toCommand(final RegisterKeyResultRequest request) {
		return new RegisterKeyResultCommand(request.projectToken(), request.keyResultName());
	}

	static UpdateKeyResultCommand toCommand(final UpdateKeyResultRequest request) {
		return new UpdateKeyResultCommand(request.keyResultToken(), request.keyResultName());
	}
}
