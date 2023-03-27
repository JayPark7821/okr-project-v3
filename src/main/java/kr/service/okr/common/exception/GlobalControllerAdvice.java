package kr.service.okr.common.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import kr.service.okr.common.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalControllerAdvice   {

	@ExceptionHandler(OkrApplicationException.class)
	public ResponseEntity<?> applicationHandler(OkrApplicationException e) {
		log.error("Error occurs {}", e.toString());
		return Response.error(e.getErrorCode().getStatus(), e.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex ) {
		log.error("Error occurs {}", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> applicationHandler(RuntimeException e) {
		log.error("Error occurs {}", e.toString());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(Response.error(ErrorCode.INTERNAL_SERVER_ERROR));
	}

}
