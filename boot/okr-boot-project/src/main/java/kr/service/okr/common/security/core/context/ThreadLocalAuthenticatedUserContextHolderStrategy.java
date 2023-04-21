package kr.service.okr.common.security.core.context;

import java.util.function.Supplier;

import org.springframework.util.Assert;

public class ThreadLocalAuthenticatedUserContextHolderStrategy implements AuthenticatedUserContextHolderStrategy {

	private static final ThreadLocal<Supplier<AuthenticationContext>> authenticatedUserContextHolder = new ThreadLocal<>();

	@Override
	public Supplier<AuthenticationContext> getDeferredContext() {
		Supplier<AuthenticationContext> result = authenticatedUserContextHolder.get();
		if (result == null) {
			AuthenticationContext context = createEmptyContext();
			result = () -> context;
			authenticatedUserContextHolder.set(result);
		}
		return result;
	}

	@Override
	public void setDeferredContext(final Supplier<AuthenticationContext> deferredContext) {
		Assert.notNull(deferredContext, "Only non-null Supplier instances are permitted");
		Supplier<AuthenticationContext> notNullDeferredContext = () -> {
			AuthenticationContext result = deferredContext.get();
			Assert.notNull(result, "A Supplier<User> returned null and is not allowed.");
			return result;
		};
		authenticatedUserContextHolder.set(notNullDeferredContext);
	}

	@Override
	public void clearContext() {
		authenticatedUserContextHolder.remove();
	}

	@Override
	public AuthenticationContext getContext() {
		return getDeferredContext().get();
	}

	@Override
	public void setContext(final AuthenticationContext context) {
		Assert.notNull(context, "Only non-null SecurityContext instances are permitted");
		authenticatedUserContextHolder.set(() -> context);
	}

	@Override
	public AuthenticationContext createEmptyContext() {
		return new AuthenticationContextImpl();
	}
}
