package kr.service.okr.common.logging;

import org.springframework.http.HttpMethod;
import org.springframework.web.method.HandlerMethod;

public interface HttpMethodsMappingExtractor {

	boolean support(HandlerMethod handlerMethod);

	RequestEndPoint extractEndPoint(HandlerMethod handlerMethod);

	record RequestEndPoint(String path, HttpMethod httpMethod) {
	}

}
