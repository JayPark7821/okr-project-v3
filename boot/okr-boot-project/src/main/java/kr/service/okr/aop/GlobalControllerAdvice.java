package kr.service.okr.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.service.okr.Response;
import kr.service.okr.exception.OkrApplicationException;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalControllerAdvice {

	private static final Logger log = LoggerFactory.getLogger(GlobalControllerAdvice.class);

	@ExceptionHandler(OkrApplicationException.class)
	public ResponseEntity<?> applicationHandler(OkrApplicationException e) {
		log.error("Error occurs {}", e.toString());
		return Response.response(e.getErrorCode().getHttpStatus(), e.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
		log.error("Error occurs {}", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
		return Response.response(HttpStatus.BAD_REQUEST,
			ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> applicationHandler(RuntimeException e) {
		log.error("Error occurs {}", e.getMessage());
		return Response.errorBadRequest(e.getMessage());
	}

}
