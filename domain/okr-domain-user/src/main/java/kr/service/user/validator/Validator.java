package kr.service.user.validator;

import java.util.Objects;

import kr.service.user.ProviderType;
import kr.service.user.exception.ErrorCode;
import kr.service.user.exception.OkrUserDomainException;
import kr.service.user.user.domain.JobField;

public class Validator {

	private static final int MAX_LENGTH_OF_USERNAME = 20;

	public static void validateProviderType(final ProviderType providerType) {
		if (Objects.isNull(providerType))
			throw new OkrUserDomainException(ErrorCode.PROVIDER_TYPE_IS_REQUIRED);
	}

	public static void validateEmail(final String email) {
		if (Objects.isNull(email) || email.isBlank())
			throw new OkrUserDomainException(ErrorCode.EMAIL_IS_REQUIRED);
	}

	public static void validateUsername(final String username) {
		if (Objects.isNull(username) || username.isBlank() || username.length() > MAX_LENGTH_OF_USERNAME)
			throw new OkrUserDomainException(ErrorCode.USERNAME_IS_REQUIRED);
	}

	public static void validateId(final String id) {
		if (Objects.isNull(id) || id.isBlank())
			throw new OkrUserDomainException(ErrorCode.ID_IS_REQUIRED);
	}

	public static void validateJobField(final JobField jobField) {
		if (Objects.isNull(jobField))
			throw new OkrUserDomainException(ErrorCode.JOB_FIELD_IS_REQUIRED);
	}

	public static void validateRefreshToken(String refreshToken) {
		if (Objects.isNull(refreshToken) || refreshToken.isBlank())
			throw new OkrUserDomainException(ErrorCode.INTERNAL_SERVER_ERROR);
	}
}
