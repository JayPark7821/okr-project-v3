package kr.service.user.user.domain;

import static kr.service.user.validator.Validator.*;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.util.TokenGenerator;
import kr.service.user.ProviderType;
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
	private RoleType roleType;
	private String password;
	private JobField jobField;

	@Builder
	private User(final Long userSeq, final String userId, final String username, final String email,
		final String profileImage, final ProviderType providerType,
		final RoleType roleType, final String password, final JobField jobField) {

		if (userSeq == null || userId == null || username == null || email == null || providerType == null
			|| roleType == null || password == null || jobField == null)
			throw new OkrApplicationException(ErrorCode.INTERNAL_SERVER_ERROR);

		this.userSeq = userSeq;
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.profileImage = profileImage;
		this.providerType = providerType;
		this.roleType = roleType;
		this.password = password;
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
		this.roleType = RoleType.USER;
		this.password = TokenGenerator.randomCharacter(20);
		this.jobField = jobField;
	}

	public void validateProvider(ProviderType providerType) {
		if (this.providerType != providerType) {
			throw new OkrApplicationException(ErrorCode.MISS_MATCH_PROVIDER, this.providerType.name());
		}
	}

}
