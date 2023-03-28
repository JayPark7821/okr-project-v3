package kr.service.okr.interfaces;

import org.springframework.security.core.Authentication;

import kr.service.okrcommon.common.exception.ErrorCode;
import kr.service.okrcommon.common.exception.OkrApplicationException;
import kr.service.okrcommon.common.utils.ClassUtils;
import kr.service.okr.domain.user.User;

public abstract class AbstractController {
	public Long getUserSeqFromAuthentication(Authentication authentication) {
		return ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_FAILED))
			.getUserSeq();
	}

}
