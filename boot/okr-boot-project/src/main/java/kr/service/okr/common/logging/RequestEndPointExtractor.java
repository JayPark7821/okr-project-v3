package kr.service.okr.common.logging;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RequestEndPointExtractor {

	private final List<HttpMethodsMappingExtractor> httpMethodsMappingExtractors;

	public HttpMethodsMappingExtractor.RequestEndPoint extractEndPoint(final HandlerMethod handlerMethod) {
		return httpMethodsMappingExtractors.stream()
			.filter(httpMethodsMappingExtractor -> httpMethodsMappingExtractor.support(handlerMethod))
			.findFirst()
			.map(httpMethodsMappingExtractor -> httpMethodsMappingExtractor.extractEndPoint(handlerMethod))
			.orElseThrow(() -> new IllegalArgumentException("지원하지 않는 메소드입니다."));
	}
}
