package kr.service.okr.user.usecase.guest;

import kr.service.okr.user.domain.Guest;
import kr.service.okr.user.enums.ProviderType;

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
