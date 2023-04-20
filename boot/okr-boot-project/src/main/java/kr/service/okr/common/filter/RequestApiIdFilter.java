package kr.service.okr.common.filter;

import java.io.IOException;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class RequestApiIdFilter implements Filter {

	static final String TRX_ID_HEADER = "x-trx-log-id";
	static final String TRX_ID = "trxId";

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws
		IOException,
		ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;

		final String requestIp = httpServletRequest.getRemoteAddr();
		final String method = httpServletRequest.getMethod();
		final String requestUri = httpServletRequest.getRequestURI();
		final String trxId = httpServletRequest.getHeader(TRX_ID_HEADER);

		MDC.put(TRX_ID, trxId);
		log.info("Request Started --> [TRX_ID] : {} , [IP] : {} , [METHOD] : {}|{}", trxId, requestIp, method,
			requestUri);

		chain.doFilter(request, response);

		MDC.remove(TRX_ID);
	}
}