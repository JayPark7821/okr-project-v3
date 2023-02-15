package kr.jay.okrver3.common.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import kr.jay.okrver3.common.utils.HeaderUtil;
import kr.jay.okrver3.common.utils.JwtTokenUtils;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

	private final String key;
	private final UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {

			String token = HeaderUtil.getToken("Authorization", request);
			if (JwtTokenUtils.isExpired(token, key)) {
				log.error("Key is Expired");
				filterChain.doFilter(request, response);
				return;
			}

			String email = JwtTokenUtils.getEmail(token, key);
			User user = userService.findByEmail(email)
				.orElseThrow(() -> {
					log.error("User does not exist");
					return new ResponseStatusException(HttpStatus.UNAUTHORIZED);
				});

			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				user, null, user.getAuthorities());
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);

		} catch (RuntimeException e) {
			log.error("Error occurs while validating. {}", e.toString());
			filterChain.doFilter(request, response);
			return;
		}
		filterChain.doFilter(request, response);
	}
}
