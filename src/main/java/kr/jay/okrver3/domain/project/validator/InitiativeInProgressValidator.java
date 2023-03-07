package kr.jay.okrver3.domain.project.validator;

import org.springframework.stereotype.Component;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.common.utils.ClassUtils;
import kr.jay.okrver3.domain.project.aggregate.initiative.Initiative;

@Component
public class InitiativeInProgressValidator implements ProjectValidator {

	@Override
	public boolean support(ProjectValidatorType type) {
		return ProjectValidatorType.VALIDATE_INITIATIVE_IN_PROGRESS == type;
	}

	@Override
	public void validate(Object... args) {
		Initiative initiative = ClassUtils.getSafeCastInstance(args, Initiative.class);

		if (initiative.isDone())
			throw new OkrApplicationException(ErrorCode.FINISHED_INITIATIVE);
	}

}
