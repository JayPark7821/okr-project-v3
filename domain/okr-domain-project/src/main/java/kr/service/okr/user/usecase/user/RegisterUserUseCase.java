package kr.service.okr.user.usecase.user;

import kr.service.okr.user.domain.User;

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
