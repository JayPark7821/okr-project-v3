package kr.service.okr.user.usecase.user;

public interface RegisterUserUseCase {
	LoginInfo command(Command command);

	record Command(
		String guestUuid,
		String username,
		String email,
		String jobField
	) {
	}
}
