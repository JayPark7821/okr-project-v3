package kr.service.okr.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import kr.service.okr.exception.ErrorCode;

public class Response<T> {

	public static ResponseEntity<String> error(HttpStatus status, String message) {
		return ResponseEntity.status(status)
			.body(message);
	}

	public static ResponseEntity<String> error(ErrorCode errorCode) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(errorCode.getMessage());
	}

	public static <T> ResponseEntity<T> successOk(T result) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(result);
	}

	public static <T> ResponseEntity<T> successCreated(T result) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(result);
	}

	public static ResponseEntity<String> success(HttpStatus status) {
		return ResponseEntity.status(status)
			.body("SUCCESS");
	}

}
