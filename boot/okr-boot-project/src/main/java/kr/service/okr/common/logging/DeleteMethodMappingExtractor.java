package kr.service.okr.common.logging;

import java.util.Arrays;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.method.HandlerMethod;

@Component
public class DeleteMethodMappingExtractor implements HttpMethodsMappingExtractor {

	@Override
	public boolean support(final HandlerMethod handlerMethod) {
		final DeleteMapping methodAnnotation = handlerMethod.getMethodAnnotation(DeleteMapping.class);
		return methodAnnotation != null;
	}

	@Override
	public RequestEndPoint extractEndPoint(final HandlerMethod handlerMethod) {
		final DeleteMapping deleteMapping = handlerMethod.getMethodAnnotation(DeleteMapping.class);
		final String api = Arrays.stream(deleteMapping.value())
			.findAny()
			.orElseGet(() ->
				Arrays.stream(deleteMapping.path()).findAny().orElse("")
			);
		return new RequestEndPoint(api, HttpMethod.DELETE);
	}
}
