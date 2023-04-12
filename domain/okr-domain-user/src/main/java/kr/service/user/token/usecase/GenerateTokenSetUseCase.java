package kr.service.user.token.usecase;

public interface GenerateTokenSetUseCase {

	AuthTokenInfo command(String email);

	record AuthTokenInfo(
		String accessToken,
		String refreshToken

	) {
	}
}
