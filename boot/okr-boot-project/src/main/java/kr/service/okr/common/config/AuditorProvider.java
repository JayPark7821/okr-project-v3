package kr.service.okr.common.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import kr.service.okr.AuthenticationInfo;
import kr.service.okr.common.security.core.context.AuthenticatedUserContextHolder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AuditorProvider implements AuditorAware<String> {
	@Override
	public Optional<String> getCurrentAuditor() {
		final AuthenticationInfo authenticationInfo =
			AuthenticatedUserContextHolder.getContext().getAuthenticationInfo();
		if (ObjectUtils.isEmpty(authenticationInfo)) {
			return Optional.empty();
		}
		return Optional.of(authenticationInfo.userEmail());
	}
}