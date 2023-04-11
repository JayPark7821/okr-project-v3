package kr.service.user.guest.usecase;

import kr.service.user.guest.ProviderType;
import kr.service.user.guest.domain.Guest;

public interface CreateNewGuestUseCase {
	Guest createNewGuestFrom(Command command);

	record Command(
		String id,
		String username,
		String email,
		String profileImageUrl,
		ProviderType providerType
	) {
	}

}
