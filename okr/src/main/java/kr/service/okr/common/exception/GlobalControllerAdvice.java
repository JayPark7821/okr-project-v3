package kr.service.okr.common.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.service.okr.common.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalControllerAdvice {

	@ExceptionHandler(OkrApplicationException.class)
	public ResponseEntity<?> applicationHandler(OkrApplicationException e) {
		log.error("Error occurs {}", e.toString());
		return Response.error(e.getErrorCode().getStatus(), e.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
		log.error("Error occurs {}", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
		return Response.error(HttpStatus.BAD_REQUEST, ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> applicationHandler(RuntimeException e) {
		log.error("Error occurs {}", e.toString());
		return Response.error(ErrorCode.INTERNAL_SERVER_ERROR);
	}

}
