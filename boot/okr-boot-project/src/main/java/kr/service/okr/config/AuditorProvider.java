package kr.service.okr.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import kr.service.okr.user.domain.User;
import kr.service.okr.util.ClassUtils;

@Component
public class AuditorProvider implements AuditorAware<String> {
	@Override
	public Optional<String> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (ObjectUtils.isEmpty(authentication) || !authentication.isAuthenticated()) {
			return Optional.empty();
		}

		return ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.map(user -> user.getUserSeq().toString());
	}
}