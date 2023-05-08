package kr.service.okr.common.logging;

import java.util.Arrays;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.method.HandlerMethod;

@Component
public class PutMethodMappingExtractor implements HttpMethodsMappingExtractor {

	@Override
	public boolean support(final HandlerMethod handlerMethod) {
		final PutMapping methodAnnotation = handlerMethod.getMethodAnnotation(PutMapping.class);
		return methodAnnotation != null;
	}

	@Override
	public RequestEndPoint extractEndPoint(final HandlerMethod handlerMethod) {
		final PutMapping putMapping = handlerMethod.getMethodAnnotation(PutMapping.class);
		final String api = Arrays.stream(putMapping.value())
			.findAny()
			.orElseGet(() ->
				Arrays.stream(putMapping.path()).findAny().orElse("")
			);
		return new RequestEndPoint(api, HttpMethod.PUT);
	}
}
