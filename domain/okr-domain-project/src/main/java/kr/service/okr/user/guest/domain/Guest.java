package kr.service.okr.user.guest.domain;

import static kr.service.okr.user.validator.Validator.*;

import kr.service.okr.user.enums.ProviderType;
import kr.service.okr.user.exception.ErrorCode;
import kr.service.okr.util.TokenGenerator;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Guest {
	private static final String GUEST_PREFIX = "guest-";

	private Long guestSeq;

	private String guestUuid;

	private String guestId;

	private String guestName;

	private String email;

	private ProviderType providerType;

	private String profileImage;

	@Builder
	private Guest(final Long guestSeq, final String guestUuid, final String guestId, final String guestName,
		final String email, final ProviderType providerType,
		final String profileImage) {
		if (guestSeq == null || guestUuid == null || guestId == null || guestName == null || email == null
			|| providerType == null)
			throw new RuntimeException(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
		this.guestSeq = guestSeq;
		this.guestUuid = guestUuid;
		this.guestId = guestId;
		this.guestName = guestName;
		this.email = email;
		this.providerType = providerType;
		this.profileImage = profileImage;
	}

	public Guest(
		final String guestId,
		final String guestName,
		final String email,
		final ProviderType providerType,
		final String profileImage
	) {
		validateId(guestId);
		validateUsername(guestName);
		validateEmail(email);
		validateProviderType(providerType);

		this.guestUuid = TokenGenerator.randomCharacterWithPrefix(GUEST_PREFIX);
		this.guestId = guestId;
		this.guestName = guestName;
		this.email = email;
		this.providerType = providerType;
		this.profileImage = profileImage;
	}

}
