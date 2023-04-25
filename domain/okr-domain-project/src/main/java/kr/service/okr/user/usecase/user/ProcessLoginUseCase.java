package kr.service.okr.user.usecase.user;

import java.util.Optional;

import kr.service.okr.user.enums.ProviderType;

public interface ProcessLoginUseCase {
	Optional<LoginInfo> command(Command command);

	record Command(
		String email,
		ProviderType providerType
	) {
		public Command(String email, String providerType) {
			this(email, ProviderType.valueOf(providerType));
		}
	}

}
