package kr.service.okr.common.config;

import static org.springframework.security.authorization.AuthenticatedAuthorizationManager.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthenticatedAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import jakarta.servlet.http.HttpServletRequest;
import kr.service.okr.common.filter.JwtTokenFilter;
import kr.service.okr.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig {

	private final UserService userService;

	@Value("${app.auth.tokenSecret}")
	private String key;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf().disable()
			.headers().frameOptions().disable()
			.and()

			.authorizeHttpRequests()
			.requestMatchers(HttpMethod.POST, "/api/*/user/login/**").permitAll()
			.requestMatchers(HttpMethod.GET, "/api/*/user/job/**").permitAll()
			.requestMatchers(HttpMethod.POST, "/api/*/user/join/**").permitAll()
			.requestMatchers( "/api/**")
			.authenticated()
			.anyRequest()
			.denyAll()
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.formLogin().disable()
			.addFilterBefore(new JwtTokenFilter(key, userService), UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling()
			.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
			.and()
			.build();
	}
	private static AuthorizationManager<RequestAuthorizationContext> hasIpAddress(String ipAddress) {
		IpAddressMatcher ipAddressMatcher = new IpAddressMatcher(ipAddress);
		return (authentication, context) -> {
			HttpServletRequest request = context.getRequest();
			return new AuthorizationDecision(ipAddressMatcher.matches(request));
		};
	}
}