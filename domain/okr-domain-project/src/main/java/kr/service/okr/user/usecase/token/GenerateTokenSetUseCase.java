package kr.service.okr.user.usecase.token;

public interface GenerateTokenSetUseCase {

	AuthTokenInfo command(String email);

	record AuthTokenInfo(
		String accessToken,
		String refreshToken

	) {
	}
}
