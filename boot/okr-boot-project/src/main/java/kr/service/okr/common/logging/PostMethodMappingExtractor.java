package kr.service.okr.common.logging;

import java.util.Arrays;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.method.HandlerMethod;

@Component
public class PostMethodMappingExtractor implements HttpMethodsMappingExtractor {

	@Override
	public boolean support(final HandlerMethod handlerMethod) {
		final PostMapping methodAnnotation = handlerMethod.getMethodAnnotation(PostMapping.class);
		return methodAnnotation != null;
	}

	@Override
	public RequestEndPoint extractEndPoint(final HandlerMethod handlerMethod) {
		final PostMapping postMapping = handlerMethod.getMethodAnnotation(PostMapping.class);
		final String api = Arrays.stream(postMapping.value())
			.findAny()
			.orElseGet(() ->
				Arrays.stream(postMapping.path()).findAny().orElse("")
			);
		return new RequestEndPoint(api, HttpMethod.POST);
	}
}
