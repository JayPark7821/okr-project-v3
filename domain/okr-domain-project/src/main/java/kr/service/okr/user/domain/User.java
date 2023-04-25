package kr.service.okr.user.domain;

import static kr.service.okr.user.validator.Validator.*;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.user.enums.JobField;
import kr.service.okr.user.enums.ProviderType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class User {
	private Long userSeq;
	private String userId;
	private String username;
	private String email;
	private String profileImage;
	private ProviderType providerType;
	private JobField jobField;

	@Builder
	private User(final Long userSeq, final String userId, final String username, final String email,
		final String profileImage, final ProviderType providerType, final JobField jobField) {

		if (userSeq == null || userId == null || username == null || email == null || providerType == null
			|| jobField == null)
			throw new OkrApplicationException(ErrorCode.INTERNAL_SERVER_ERROR);

		this.userSeq = userSeq;
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.profileImage = profileImage;
		this.providerType = providerType;
		this.jobField = jobField;
	}

	public User(
		String userId,
		String username,
		String email,
		String profileImage,
		ProviderType providerType,
		JobField jobField
	) {
		validateId(userId);
		validateUsername(username);
		validateEmail(email);
		validateProviderType(providerType);
		validateJobField(jobField);

		this.userId = userId;
		this.username = username;
		this.email = email;
		this.profileImage = profileImage;
		this.providerType = providerType;
		this.jobField = jobField;
	}

	public static User joinNewUser(Guest guest, String username, String email, String jobField) {
		if (!guest.getEmail().equals(email)) {
			throw new OkrApplicationException(ErrorCode.INVALID_JOIN_INFO);
		}
		return new User(
			guest.getGuestId(),
			username,
			guest.getEmail(),
			guest.getProfileImage(),
			guest.getProviderType(),
			JobField.valueOf(jobField)
		);
	}

	public boolean validateProvider(ProviderType providerType) {
		if (this.providerType != providerType) {
			throw new OkrApplicationException(ErrorCode.MISS_MATCH_PROVIDER, this.providerType.name());
		}
		return true;
	}

}
