package kr.service.user.config;

import java.util.Optional;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import kr.service.jwt.JwtUtils;
import kr.service.okr.util.HeaderUtil;

@Component
public class AuditorProvider implements AuditorAware<String> {

	@Value("${app.auth.tokenSecret}")
	private String secretKey;

	@Override
	public Optional<String> getCurrentAuditor() {
		try {
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			final String jwt = HeaderUtil.getTokenFrom(request.getHeader(HttpHeaders.AUTHORIZATION));
			if (ObjectUtils.isEmpty(jwt)) {
				return Optional.empty();
			}
			return Optional.of(JwtUtils.getEmail(jwt, secretKey));

		} catch (NullPointerException e) {
			return Optional.empty();
		}

	}
}