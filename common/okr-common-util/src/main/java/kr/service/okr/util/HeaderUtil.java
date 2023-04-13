package kr.service.okr.util;

public class HeaderUtil {

	private final static String TOKEN_PREFIX = "Bearer ";

	public static String getTokenFrom(String authHeader) {
		return
			(authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) ?
				authHeader.substring(TOKEN_PREFIX.length()) :
				null;
	}
}
