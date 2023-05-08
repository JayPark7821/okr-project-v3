package kr.service.okr.common.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class RequestApiIdFilter implements Filter {
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws
		IOException,
		ServletException {
		final ContentCachingRequestWrapper wrappedRequest =
			new ContentCachingRequestWrapper((HttpServletRequest)request);
		final ContentCachingResponseWrapper wrappedResponse =
			new ContentCachingResponseWrapper((HttpServletResponse)response);

		chain.doFilter(wrappedRequest, wrappedResponse);
		wrappedResponse.copyBodyToResponse();

	}
}