package kr.service.okr.common.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

public class RequestMatcherContainer {

	private final PathMatcher pathMatcher;
	private final List<RequestPath> notRequireAuthPaths;

	public RequestMatcherContainer() {
		this.pathMatcher = new AntPathMatcher();
		this.notRequireAuthPaths = new ArrayList<>();
	}

	public boolean doesPathRequireAuth(final String requestUri, final HttpMethod method) {
		return this.notRequireAuthPaths.stream()
			.noneMatch(requestPath ->
				requestPath.methods().equals(method) && pathMatcher.match(requestPath.requestPattern(), requestUri)
			);
	}

	public void includeNotRequireAuthPath(final String requestPattern, final HttpMethod methods) {
		this.notRequireAuthPaths.add(new RequestPath(requestPattern, methods));
	}

	record RequestPath(
		String requestPattern,
		HttpMethod methods
	) {
	}
}
