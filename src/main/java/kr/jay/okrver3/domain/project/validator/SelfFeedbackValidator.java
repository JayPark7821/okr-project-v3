package kr.jay.okrver3.domain.project.validator;

import org.springframework.stereotype.Component;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.common.utils.ClassUtils;
import kr.jay.okrver3.domain.project.aggregate.team.TeamMember;

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
