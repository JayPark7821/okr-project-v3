package kr.service.okr.project.exception;

public class OkrProjectDomainException extends RuntimeException {
	private final String message;

	public OkrProjectDomainException(final ErrorCode errorCode, final String... args) {
		this.message = errorCode.getMessage().formatted(args);

	}

	public OkrProjectDomainException(final ErrorCode errorCode) {
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
