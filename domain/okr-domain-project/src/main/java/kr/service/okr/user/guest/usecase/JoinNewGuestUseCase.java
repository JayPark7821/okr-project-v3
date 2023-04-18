package kr.service.okr.user.guest.usecase;

import kr.service.okr.user.enums.ProviderType;
import kr.service.okr.user.guest.domain.Guest;

public interface JoinNewGuestUseCase {
	Guest command(Command command);

	record Command(
		String id,
		String username,
		String email,
		String profileImageUrl,
		ProviderType providerType
	) {
	}

}
