package kr.service.okrgatewayservice;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

	public GlobalFilter() {
		super(Config.class);
	}

	@Getter
	@Setter
	public static class Config {
		// put the configuration properties
		private String baseMessage;
		private boolean preLogger;
		private boolean postLogger;
	}

	@Override
	public GatewayFilter apply(Config config) {
		// Custom Pre filter
		return ((exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			ServerHttpResponse response = exchange.getResponse();

			if (config.isPreLogger()) {
				log.info("request id -> {}  Method: {} || RequestUrl: {} ", request.getId(), request.getMethod(),
					request.getURI());
			}
			//Custom Post Filter
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				if (config.isPostLogger()) {
					log.info("Global Filter End: response code -> {} ", response.getStatusCode());
				}
			}));
		});
	}
}
