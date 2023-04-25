package kr.service.okr.user.usecase.guest;

import kr.service.okr.user.enums.ProviderType;
import kr.service.okr.user.usecase.user.LoginInfo;

public interface RegisterGuestUseCase {
	LoginInfo command(Command command);

	record Command(
		String id,
		String username,
		String email,
		String profileImageUrl,
		ProviderType providerType
	) {
	}

}
