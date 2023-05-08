package kr.service.okr.common.logging;

import java.util.Arrays;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.method.HandlerMethod;

@Component
public class GetMethodMappingExtractor implements HttpMethodsMappingExtractor {

	@Override
	public boolean support(final HandlerMethod handlerMethod) {
		final GetMapping methodAnnotation = handlerMethod.getMethodAnnotation(GetMapping.class);
		return methodAnnotation != null;
	}

	@Override
	public RequestEndPoint extractEndPoint(final HandlerMethod handlerMethod) {
		final GetMapping getMapping = handlerMethod.getMethodAnnotation(GetMapping.class);
		final String api = Arrays.stream(getMapping.value())
			.findAny()
			.orElseGet(() ->
				Arrays.stream(getMapping.path()).findAny().orElse("")
			);
		return new RequestEndPoint(api, HttpMethod.GET);
	}
}
