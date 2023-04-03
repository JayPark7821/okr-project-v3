package kr.service.okr.common.exception;

public class OkrApplicationException extends RuntimeException {

	private final ErrorCode errorCode;
	private final String message;

	public OkrApplicationException(ErrorCode errorCode) {
		this.errorCode = errorCode;
		this.message = null;
	}

	public OkrApplicationException(ErrorCode errorCode, String msg) {
		this.errorCode = errorCode;
		this.message = msg;
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

	public ErrorCode getErrorCode() {
		return errorCode;
	}
}
