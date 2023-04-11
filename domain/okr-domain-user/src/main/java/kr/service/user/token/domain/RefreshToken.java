package kr.service.user.token.domain;

public record RefreshToken(
	Long refreshTokenSeq,
	String userEmail,
	String refreshToken
) {
}
