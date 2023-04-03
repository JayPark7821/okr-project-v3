package kr.service.okrcommon.common.utils;

import org.springframework.http.HttpHeaders;

import jakarta.servlet.http.HttpServletRequest;

public class HeaderUtil {

	private final static String TOKEN_PREFIX = "Bearer ";

	public static String getToken(HttpServletRequest request) {
		String headerValue = request.getHeader(HttpHeaders.AUTHORIZATION);
		return
			(headerValue != null && headerValue.startsWith(TOKEN_PREFIX)) ?
				headerValue.substring(TOKEN_PREFIX.length()) :
				null;
	}
}
