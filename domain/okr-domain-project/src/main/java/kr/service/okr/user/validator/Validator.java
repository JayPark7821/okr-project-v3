package kr.service.okr.user.validator;

import java.util.Objects;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.user.enums.JobField;
import kr.service.okr.user.enums.ProviderType;

public class Validator {

	private static final int MAX_LENGTH_OF_USERNAME = 20;

	public static void validateProviderType(final ProviderType providerType) {
		if (Objects.isNull(providerType))
			throw new OkrApplicationException(ErrorCode.PROVIDER_TYPE_IS_REQUIRED);
	}

	public static void validateEmail(final String email) {
		if (Objects.isNull(email) || email.isBlank())
			throw new OkrApplicationException(ErrorCode.EMAIL_IS_REQUIRED);
	}

	public static void validateUsername(final String username) {
		if (Objects.isNull(username) || username.isBlank() || username.length() > MAX_LENGTH_OF_USERNAME)
			throw new OkrApplicationException(ErrorCode.USERNAME_IS_REQUIRED);
	}

	public static void validateId(final String id) {
		if (Objects.isNull(id) || id.isBlank())
			throw new OkrApplicationException(ErrorCode.ID_IS_REQUIRED);
	}

	public static void validateJobField(final JobField jobField) {
		if (Objects.isNull(jobField))
			throw new OkrApplicationException(ErrorCode.JOB_FIELD_IS_REQUIRED);
	}

	public static void validateRefreshToken(String refreshToken) {
		if (Objects.isNull(refreshToken) || refreshToken.isBlank())
			throw new OkrApplicationException(ErrorCode.INTERNAL_SERVER_ERROR);
	}
}
