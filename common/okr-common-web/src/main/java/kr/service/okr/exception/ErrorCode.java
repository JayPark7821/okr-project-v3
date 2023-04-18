package kr.service.okr.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	REQUIRED_DATE_VALUE(HttpStatus.BAD_REQUEST, "날짜는 필수 값 입니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.BAD_REQUEST, "서버에 오류가 발생했습니다."),
	INVALID_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다.");

	final String message;
	final HttpStatus httpStatus;

	ErrorCode(final HttpStatus httpStatus, final String message) {
		this.message = message;
		this.httpStatus = httpStatus;
	}

	public String getMessage() {
		return message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}
