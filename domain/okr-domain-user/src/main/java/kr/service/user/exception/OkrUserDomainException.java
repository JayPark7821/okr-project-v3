package kr.service.user.exception;

public class OkrUserDomainException extends RuntimeException {
	private final String message;

	public OkrUserDomainException(final ErrorCode errorCode, final String... args) {
		this.message = errorCode.getMessage().formatted(args);

	}

	public OkrUserDomainException(final ErrorCode errorCode) {
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
