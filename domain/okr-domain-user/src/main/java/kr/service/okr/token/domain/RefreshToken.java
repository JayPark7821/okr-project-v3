package kr.service.okr.token.domain;

public record RefreshToken(
	Long refreshTokenSeq,
	String userEmail,
	String refreshToken
) {
}
