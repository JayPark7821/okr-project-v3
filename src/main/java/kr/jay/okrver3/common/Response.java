package kr.jay.okrver3.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import kr.jay.okrver3.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response<T> {

	public static ResponseEntity<String> error(HttpStatus status, String message) {
		return ResponseEntity.status(status)
			.body(message);
	}

	public static ResponseEntity<String> error(ErrorCode errorCode) {
		return ResponseEntity.status(errorCode.getStatus())
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
