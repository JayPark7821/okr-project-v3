package kr.service.okr.config;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import feign.Response;
import feign.codec.ErrorDecoder;
import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;

@Component
public class UserFeignErrorDecoder implements ErrorDecoder {

	private final ErrorDecoder errorDecoder = new Default();

	@Override
	public Exception decode(final String methodKey, final Response response) {
		final HttpStatus httpStatus = HttpStatus.resolve(response.status());
		if (httpStatus == HttpStatus.UNAUTHORIZED) {
			return new OkrApplicationException(ErrorCode.EXPIRED_TOKEN);
		}
		return errorDecoder.decode(methodKey, response);
	}
}
