package kr.service.token.domain;

public record RefreshToken(
	Long refreshTokenSeq,
	String userEmail,
	String refreshToken
) {
}
