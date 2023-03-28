package kr.service.okr.domain.project.validator;

import org.springframework.stereotype.Component;

import kr.service.okrcommon.common.exception.ErrorCode;
import kr.service.okrcommon.common.exception.OkrApplicationException;
import kr.service.okrcommon.common.utils.ClassUtils;
import kr.service.okr.domain.project.Project;

@Component
public class ProjectPeriodValidator implements ProjectValidator {

	@Override
	public boolean support(ProjectValidatorType type) {
		return ProjectValidatorType.VALIDATE_PROJECT_PERIOD == type;
	}

	@Override
	public void validate(Object... args) {

		Project project = ClassUtils.getSafeCastInstance(args, Project.class);
		
		if (!project.isValidUntilToday())
			throw new OkrApplicationException(ErrorCode.NOT_UNDER_PROJECT_DURATION);
	}

}
