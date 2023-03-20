package kr.jay.okrver3.interfaces;

import org.springframework.security.core.Authentication;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.common.utils.ClassUtils;
import kr.jay.okrver3.domain.user.User;

public abstract class AbstractController {
	public Long getUserSeqFromAuthentication(Authentication authentication) {
		return ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_FAILED))
			.getUserSeq();
	}

}
