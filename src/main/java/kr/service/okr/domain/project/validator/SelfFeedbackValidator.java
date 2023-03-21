package kr.service.okr.domain.project.validator;

import org.springframework.stereotype.Component;

import kr.service.okr.common.exception.ErrorCode;
import kr.service.okr.common.exception.OkrApplicationException;
import kr.service.okr.common.utils.ClassUtils;
import kr.service.okr.domain.project.aggregate.team.TeamMember;

@Component
public class SelfFeedbackValidator implements ProjectValidator {

	@Override
	public boolean support(ProjectValidatorType type) {
		return ProjectValidatorType.VALIDATE_SELF_FEEDBACK == type;
	}

	@Override
	public void validate(Object... args) {

		TeamMember teamMember = ClassUtils.getSafeCastInstance(args, TeamMember.class);
		Long requesterSeq = ClassUtils.getSafeCastInstance(args, Long.class);

		if (requesterSeq.equals(teamMember.getUser().getUserSeq()))
			throw new OkrApplicationException(ErrorCode.MOT_AVAIL_FEEDBACK_SELF);
	}

}
