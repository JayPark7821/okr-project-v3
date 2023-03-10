package kr.jay.okrver3.common.utils;

import javax.servlet.http.HttpServletRequest;

public class HeaderUtil {

	private final static String HEADER_AUTHORIZATION = "Authorization";
	private final static String REFRESH_TOKEN = "refresh-token";
	private final static String TOKEN_PREFIX = "Bearer ";

	public static String getAccessToken(HttpServletRequest request) {
		String headerValue = request.getHeader(HEADER_AUTHORIZATION);

		if (headerValue == null) {
			return null;
		}

		if (headerValue.startsWith(TOKEN_PREFIX)) {
			return headerValue.substring(TOKEN_PREFIX.length());
		}

		return null;
	}

	public static String getToken(String tokenKind, HttpServletRequest request) {
		String headerValue = request.getHeader(tokenKind);

		if (headerValue == null) {
			return null;
		}

		if (headerValue.startsWith(TOKEN_PREFIX)) {
			return headerValue.substring(TOKEN_PREFIX.length());
		}

		return null;
	}
}
