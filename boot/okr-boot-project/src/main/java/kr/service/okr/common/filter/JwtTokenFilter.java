// package kr.service.okr.common.filter;
//
// import java.io.IOException;
// import java.util.List;
//
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
// import org.springframework.web.filter.OncePerRequestFilter;
// import org.springframework.web.server.ResponseStatusException;
//
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import kr.service.okr.user.usecase.token.QueryAuthenticationUseCase;
// import kr.service.okr.user.domain.User;
// import kr.service.okr.user.usecase.user.QueryUserUseCase;
// import kr.service.okr.util.HeaderUtil;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// @RequiredArgsConstructor
// public class JwtTokenFilter extends OncePerRequestFilter {
//
// 	private final String key;
// 	private final QueryAuthenticationUseCase queryAuthenticationUseCase;
// 	private final QueryUserUseCase queryUserUseCase;
//
// 	@Override
// 	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
// 		FilterChain filterChain) throws ServletException, IOException {
// 		try {
//
// 			String token = HeaderUtil.getTokenFrom(request.getHeader(HttpHeaders.AUTHORIZATION));
//
// 			String email = queryAuthenticationUseCase.queryEmailBy(token)
// 				.orElseThrow(() -> {
// 					log.error("token is invalid");
// 					return new ResponseStatusException(HttpStatus.UNAUTHORIZED);
// 				});
// 			User user = queryUserUseCase.queryUserBy(email)
// 				.orElseThrow(() -> {
// 					log.error("User does not exist");
// 					return new ResponseStatusException(HttpStatus.UNAUTHORIZED);
// 				});
//
// 			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
// 				user, null, List.of(new SimpleGrantedAuthority(user.getRoleType().getValue())));
// 			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
// 			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//
// 		} catch (RuntimeException e) {
// 			log.error("Error occurs while validating. {}", e.toString());
// 			filterChain.doFilter(request, response);
// 			return;
// 		}
// 		filterChain.doFilter(request, response);
// 	}
// }
