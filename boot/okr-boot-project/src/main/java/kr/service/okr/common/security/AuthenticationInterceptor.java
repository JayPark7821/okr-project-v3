package kr.service.okr.common.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.service.okr.common.security.core.context.AuthenticatedUserContextHolder;
import kr.service.okr.common.security.core.context.AuthenticatedUserContextHolderStrategy;
import kr.service.okr.common.security.core.context.AuthenticationInfo;
import kr.service.okr.user.auth.usecase.QueryAuthenticationUseCase;
import kr.service.okr.user.user.domain.User;
import kr.service.okr.user.user.usecase.QueryUserUseCase;
import kr.service.okr.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

	private AuthenticatedUserContextHolderStrategy securityContextHolderStrategy =
		AuthenticatedUserContextHolder.getContextHolderStrategy();

	private final QueryAuthenticationUseCase queryAuthenticationUseCase;
	private final QueryUserUseCase queryUserUseCase;

	public AuthenticationInterceptor(
		final QueryAuthenticationUseCase queryAuthenticationUseCase,
		final QueryUserUseCase queryUserUseCase
	) {
		this.queryAuthenticationUseCase = queryAuthenticationUseCase;
		this.queryUserUseCase = queryUserUseCase;
	}

	@Override
	public boolean preHandle(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final Object handler
	) throws Exception {

		String token = HeaderUtil.getTokenFrom(request.getHeader(HttpHeaders.AUTHORIZATION));

		String email = queryAuthenticationUseCase.queryEmailBy(token)
			.orElseThrow(() -> {
				log.error("token is invalid");
				return new ResponseStatusException(HttpStatus.UNAUTHORIZED);
			});

		User user = queryUserUseCase.query(email)
			.orElseThrow(() -> {
				log.error("User does not exist");
				return new ResponseStatusException(HttpStatus.UNAUTHORIZED);
			});

		this.securityContextHolderStrategy.getContext().setAuthenticationInfo(new AuthenticationInfo(user));
		return true;
	}

	@Override
	public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,
		final Object handler,
		final Exception ex) throws Exception {
		this.securityContextHolderStrategy.clearContext();
	}
}
