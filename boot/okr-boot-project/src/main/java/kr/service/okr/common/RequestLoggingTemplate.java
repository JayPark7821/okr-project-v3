package kr.service.okr.common;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import lombok.Getter;

@Getter
@Component
@RequestScope
public class RequestLoggingTemplate {

	private String method;
	private String endPoint;
	private AtomicLong totalRequestTime;
	private AtomicLong queryCounts = new AtomicLong(0);

	public void addQueryCount() {
		this.queryCounts.incrementAndGet();
	}

}
