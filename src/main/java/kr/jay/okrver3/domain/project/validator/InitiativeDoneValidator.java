package kr.jay.okrver3.domain.project.validator;

import org.springframework.stereotype.Component;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.common.utils.ClassUtils;
import kr.jay.okrver3.domain.initiative.Initiative;
import kr.jay.okrver3.domain.project.Project;

@Component
public class InitiativeDoneValidator implements ProjectValidator {

	@Override
	public boolean support(ProjectValidatorType type) {
		return ProjectValidatorType.VALIDATE_INITIATIVE_DONE == type;
	}

	@Override
	public void validate(Project project, Object object) {

		Initiative initiative = ClassUtils.getSafeCastInstance(object, Initiative.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_FAILED));

		if (initiative.isDone())
			throw new OkrApplicationException(ErrorCode.FINISHED_INITIATIVE);
	}

}
