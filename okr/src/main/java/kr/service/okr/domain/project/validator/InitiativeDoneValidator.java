package kr.service.okr.domain.project.validator;

import org.springframework.stereotype.Component;

import kr.service.okr.common.exception.ErrorCode;
import kr.service.okr.common.exception.OkrApplicationException;
import kr.service.okr.common.utils.ClassUtils;
import kr.service.okr.domain.project.aggregate.initiative.Initiative;

@Component
public class InitiativeDoneValidator implements ProjectValidator {

	@Override
	public boolean support(ProjectValidatorType type) {
		return ProjectValidatorType.VALIDATE_INITIATIVE_DONE == type;
	}

	@Override
	public void validate(Object... args) {
		Initiative initiative = ClassUtils.getSafeCastInstance(args, Initiative.class);

		if (!initiative.isDone())
			throw new OkrApplicationException(ErrorCode.NOT_FINISHED_INITIATIVE);
	}

}
