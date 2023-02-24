package kr.jay.okrver3.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OkrApplicationException extends RuntimeException {

	private final ErrorCode errorCode;
	private String message;

	public OkrApplicationException(ErrorCode errorCode) {
		this.errorCode = errorCode;
		this.message = null;
	}

	@Override
	public String getMessage() {
		if (message == null) {
			return errorCode.getMessage();
		}
		return String.format("%s, %s", errorCode.getMessage(), message);
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}
