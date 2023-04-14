package kr.service.okr.config;

import java.util.Optional;

import org.apache.http.HttpHeaders;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import kr.service.jwt.JwtService;
import kr.service.okr.util.HeaderUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AuditorProvider implements AuditorAware<String> {

	private final JwtService jwtService;

	@Override
	public Optional<String> getCurrentAuditor() {
		try {
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			final String jwt = HeaderUtil.getTokenFrom(request.getHeader(HttpHeaders.AUTHORIZATION));
			if (ObjectUtils.isEmpty(jwt)) {
				return Optional.empty();
			}
			return Optional.of(jwtService.getEmail(jwt));

		} catch (NullPointerException e) {
			return Optional.empty();
		}

	}
}