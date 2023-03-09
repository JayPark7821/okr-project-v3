package kr.jay.okrver3.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import kr.jay.okrver3.common.filter.JwtTokenFilter;
import kr.jay.okrver3.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig {

	private final UserService userService;

	@Value("${app.auth.tokenSecret}")
	private String key;

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().regexMatchers("^(?!/api/).*")
			.antMatchers(HttpMethod.POST, "/api/*/user/login/**")
			.antMatchers(HttpMethod.GET, "/api/*/user/job/**")
			.antMatchers(HttpMethod.POST, "/api/*/user/join/**");
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf().disable()
			.headers().frameOptions().disable().and()
			.authorizeRequests()
			.antMatchers("/api/**").authenticated()
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

}