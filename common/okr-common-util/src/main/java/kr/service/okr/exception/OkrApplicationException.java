package kr.service.okr.exception;

public class OkrApplicationException extends RuntimeException {

	private final ErrorCode errorCode;
	private final String message;

	public OkrApplicationException(final ErrorCode errorCode, final String... args) {
		this.errorCode = errorCode;
		this.message = errorCode.getMessage().formatted(args);

	}

	public OkrApplicationException(final ErrorCode errorCode) {
		this.errorCode = errorCode;
		this.message = errorCode.getMessage();
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}
