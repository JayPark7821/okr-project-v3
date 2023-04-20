package kr.service.okr.user.auth.usecase;

public interface GenerateTokenSetUseCase {

	AuthTokenInfo command(String email);

	record AuthTokenInfo(
		String accessToken,
		String refreshToken

	) {
	}
}
