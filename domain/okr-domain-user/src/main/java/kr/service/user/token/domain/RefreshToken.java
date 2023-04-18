package kr.service.user.token.domain;

import static kr.service.user.validator.Validator.*;

import kr.service.user.exception.ErrorCode;
import kr.service.user.exception.OkrUserDomainException;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RefreshToken {
	private Long refreshTokenSeq;
	private String userEmail;
	private String refreshToken;

	@Builder
	private RefreshToken(final Long refreshTokenSeq, final String userEmail, final String refreshToken) {
		if (refreshTokenSeq == null || userEmail == null || refreshToken == null)
			throw new OkrUserDomainException(ErrorCode.INTERNAL_SERVER_ERROR);
		this.refreshTokenSeq = refreshTokenSeq;
		this.userEmail = userEmail;
		this.refreshToken = refreshToken;
	}

	public RefreshToken(final String userEmail, final String refreshToken) {
		validateEmail(userEmail);
		validateRefreshToken(refreshToken);
		this.userEmail = userEmail;
		this.refreshToken = refreshToken;
	}

	public void updateRefreshToken(final String refreshToken) {
		validateRefreshToken(refreshToken);
		this.refreshToken = refreshToken;
	}
}