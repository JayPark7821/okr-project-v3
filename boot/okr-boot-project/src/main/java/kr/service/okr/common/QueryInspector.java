package kr.service.okr.common;

import java.util.Objects;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class QueryInspector implements StatementInspector {

	private final RequestLoggingTemplate requestLoggingTemplate;

	@Override
	public String inspect(String sql) {
		if (isRequested()) {
			requestLoggingTemplate.addQueryCount();
		}
		return sql;
	}

	private boolean isRequested() {
		return Objects.nonNull(RequestContextHolder.getRequestAttributes());
	}
}
