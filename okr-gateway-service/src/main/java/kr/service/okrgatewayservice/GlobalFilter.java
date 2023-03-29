package kr.service.okrgatewayservice;

import java.util.UUID;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

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

		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			ServerHttpResponse response = exchange.getResponse();

			final String trxId = UUID.randomUUID().toString().substring(0, 8);

			final ServerHttpRequest builtRequest = request.mutate()
				.header("x-trx-log-id", trxId)
				.build();

			ServerWebExchange reBuiltExchange = exchange.mutate().request(builtRequest).build();

			if (config.isPreLogger()) {
				log.info("Request Started --> [TRX_ID] : {} , [IP] : {} , [REQUEST] : {}|{}",
					trxId,
					request.getRemoteAddress(),
					request.getMethod(),
					request.getURI()
				);
			}

			return chain.filter(reBuiltExchange).then(Mono.fromRunnable(() -> {
				if (config.isPostLogger()) {
					log.info("Global Filter End: response code -> {} ", response.getStatusCode());
				}
			}));
		};

	}
}
