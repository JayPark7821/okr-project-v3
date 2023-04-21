package kr.service.okr.common.security.core.context;

import java.util.function.Supplier;

public interface AuthenticatedUserContextHolderStrategy {
	void clearContext();

	AuthenticationContext getContext();

	default Supplier<AuthenticationContext> getDeferredContext() {
		return () -> getContext();
	}

	void setContext(AuthenticationContext context);

	default void setDeferredContext(Supplier<AuthenticationContext> deferredContext) {
		setContext(deferredContext.get());
	}

	AuthenticationContext createEmptyContext();

}
