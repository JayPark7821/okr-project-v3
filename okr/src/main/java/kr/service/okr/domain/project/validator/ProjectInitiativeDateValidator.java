package kr.service.okr.domain.project.validator;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import kr.service.okr.common.exception.ErrorCode;
import kr.service.okr.common.exception.OkrApplicationException;
import kr.service.okr.common.utils.ClassUtils;
import kr.service.okr.domain.project.Project;
import kr.service.okr.domain.project.aggregate.initiative.Initiative;

@Component
public class ProjectInitiativeDateValidator implements ProjectValidator {

	@Override
	public boolean support(ProjectValidatorType type) {
		return ProjectValidatorType.VALIDATE_PROJECT_INITIATIVE_DATE == type;
	}

	@Override
	public void validate(Object... args) {

		Project project = ClassUtils.getSafeCastInstance(args, Project.class);
		Initiative initiative = ClassUtils.getSafeCastInstance(args, Initiative.class);
		
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
