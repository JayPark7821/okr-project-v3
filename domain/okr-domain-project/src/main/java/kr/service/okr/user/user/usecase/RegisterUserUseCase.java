package kr.service.okr.user.user.usecase;

import kr.service.okr.user.user.domain.User;

public interface RegisterUserUseCase {
	User command(Command command);

	record Command(
		String guestUuid,
		String username,
		String email,
		String jobField
	) {
	}
}
