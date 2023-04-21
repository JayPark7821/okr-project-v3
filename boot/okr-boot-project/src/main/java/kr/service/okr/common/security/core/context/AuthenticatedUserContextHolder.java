package kr.service.okr.common.security.core.context;

import java.util.function.Supplier;

public class AuthenticatedUserContextHolder {

	private static AuthenticatedUserContextHolderStrategy strategy;

	static {
		initialize();
	}

	private static void initialize() {
		initializeStrategy();
	}

	private static void initializeStrategy() {
		strategy = new ThreadLocalAuthenticatedUserContextHolderStrategy();
	}

	public static void clearContext() {
		strategy.clearContext();
	}

	public static AuthenticationContext getContext() {
		return strategy.getContext();
	}

	public static Supplier<AuthenticationContext> getDeferredContext() {
		return strategy.getDeferredContext();
	}

	public static void setContext(AuthenticationContext context) {
		strategy.setContext(context);
	}

	public static void setDeferredContext(Supplier<AuthenticationContext> deferredContext) {
		strategy.setDeferredContext(deferredContext);
	}

	public static AuthenticatedUserContextHolderStrategy getContextHolderStrategy() {
		return strategy;
	}

	public static AuthenticationContext createEmptyContext() {
		return strategy.createEmptyContext();
	}

}
