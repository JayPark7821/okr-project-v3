package kr.service.okr.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.service.okr.common.auth.AuthenticationInterceptor;
import kr.service.okr.common.auth.RequestMatcherInterceptor;
import kr.service.okr.user.auth.usecase.QueryAuthenticationUseCase;
import kr.service.okr.user.user.usecase.QueryUserUseCase;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

	private final QueryAuthenticationUseCase queryAuthenticationUseCase;
	private final QueryUserUseCase queryUserUseCase;

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addInterceptor(authenticationInterceptor());

	}

	private HandlerInterceptor authenticationInterceptor() {
		final RequestMatcherInterceptor interceptor = new RequestMatcherInterceptor(
			new AuthenticationInterceptor(queryAuthenticationUseCase, queryUserUseCase));

		return interceptor.includeNotRequireAuthPath("/api/*/user/login/**", HttpMethod.POST);
	}
}
