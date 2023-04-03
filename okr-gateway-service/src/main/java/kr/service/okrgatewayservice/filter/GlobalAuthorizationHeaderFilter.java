package kr.service.okrgatewayservice.filter;

import static org.springframework.http.HttpHeaders.*;

import java.util.Objects;

import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalAuthorizationHeaderFilter
	extends AbstractGatewayFilterFactory<GlobalAuthorizationHeaderFilter.Config> {

	private final Environment env;

	public static class Config {

	}

	public GlobalAuthorizationHeaderFilter(Environment env) {
		super(Config.class);
		this.env = env;
	}

	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				return onError(exchange, "no authorization header", HttpStatus.UNAUTHORIZED);
			}
			String authorizationHeader = request.getHeaders()
				.get(AUTHORIZATION)
				.get(0);
			String jwt = authorizationHeader.replace("Bearer", "");
			if (!isJwtValid(jwt)) {
				return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
			}
			return chain.filter(exchange);
		});
	}

	private boolean isJwtValid(String jwt) {
		try {
			String subject = Jwts
				.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(Objects.requireNonNull(env.getProperty("token.secret")).getBytes()))
				.build()
				.parseClaimsJws(jwt)
				.getBody()
				.getSubject();
			return subject != null && !subject.isEmpty();
		} catch (Exception e) {
			return false;
		}
	}

	// Mono, Flux -> Spring WebFlux
	private Mono<Void> onError(ServerWebExchange exchange, String error, HttpStatus httpStatus) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		log.error(error);
		return response.setComplete();
	}
}

