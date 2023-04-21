package kr.service.okr.common.security;

import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RequestMatcherInterceptor implements HandlerInterceptor {

	private final HandlerInterceptor handlerInterceptor;
	private final RequestMatcherContainer requestMatcherContainer;

	public RequestMatcherInterceptor(
		final HandlerInterceptor handlerInterceptor
	) {
		this.handlerInterceptor = handlerInterceptor;
		this.requestMatcherContainer = new RequestMatcherContainer();
	}

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
		final Object handler) throws
		Exception {

		if (requestMatcherContainer.doesPathRequireAuth(
			request.getRequestURI(),
			HttpMethod.valueOf(request.getMethod()))
		) {
			return handlerInterceptor.preHandle(request, response, handler);
		}

		return true;
	}

	public RequestMatcherInterceptor includeNotRequireAuthPath(final String requestPattern, final HttpMethod methods) {
		this.requestMatcherContainer.includeNotRequireAuthPath(requestPattern, methods);
		return this;
	}

	@Override
	public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,
		final Object handler,
		final Exception ex) throws Exception {
		handlerInterceptor.afterCompletion(request, response, handler, ex);
	}
}
