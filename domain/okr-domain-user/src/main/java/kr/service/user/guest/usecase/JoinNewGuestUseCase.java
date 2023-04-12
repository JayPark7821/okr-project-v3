package kr.service.user.guest.usecase;

import kr.service.user.ProviderType;
import kr.service.user.guest.domain.Guest;

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
