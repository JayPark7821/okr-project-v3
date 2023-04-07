package kr.service.okr.exception;

public class OkrApplicationException extends RuntimeException {

	private final ErrorCode errorCode;
	private String message;

	public OkrApplicationException(final String message, final ErrorCode errorCode) {
		this.errorCode = errorCode;
		this.message = message;
	}

	public OkrApplicationException(final ErrorCode errorCode) {
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
