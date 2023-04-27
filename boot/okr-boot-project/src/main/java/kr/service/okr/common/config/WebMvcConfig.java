package kr.service.okr.common.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.service.okr.common.security.AuthenticationArgumentResolver;
import kr.service.okr.common.security.AuthenticationInterceptor;
import kr.service.okr.common.security.RequestMatcherInterceptor;
import kr.service.okr.user.usecase.user.QueryUserUseCase;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

	private final QueryUserUseCase queryUserUseCase;

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addInterceptor(authenticationInterceptor());
	}

	@Override
	public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new AuthenticationArgumentResolver());
	}

	private HandlerInterceptor authenticationInterceptor() {
		final RequestMatcherInterceptor interceptor =
			new RequestMatcherInterceptor(
				new AuthenticationInterceptor(queryUserUseCase)
			);

		return interceptor.includeNotRequireAuthPath("/api/*/user/login/**", HttpMethod.POST)
			.includeNotRequireAuthPath("/api/*/user/join/**", HttpMethod.POST)
			.includeNotRequireAuthPath("/api/*/user/job/**", HttpMethod.GET);

	}
}
