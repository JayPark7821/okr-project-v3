package kr.jay.okrver3.domain.project.validator;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.common.utils.ClassUtils;
import kr.jay.okrver3.domain.initiative.Initiative;
import kr.jay.okrver3.domain.project.Project;

@Component
public class ProjectInitiativeDateValidator implements ProjectValidator {

	@Override
	public boolean support(ProjectValidatorType type) {
		return ProjectValidatorType.VALIDATE_PROJECT_INITIATIVE_DATE == type;
	}

	@Override
	public void validate(Project project, Object object) {

		Initiative initiative = ClassUtils.getSafeCastInstance(object, Initiative.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_USER_FAILED));

		LocalDate edt = initiative.getEdt();
		LocalDate sdt = initiative.getSdt();
		if (edt.isBefore(project.getStartDate()) ||
			edt.isAfter(project.getEndDate()) ||
			sdt.isBefore(project.getStartDate()) ||
			sdt.isAfter(project.getEndDate())
		) {
			throw new OkrApplicationException(ErrorCode.INVALID_INITIATIVE_DATE);
		}
	}

}
