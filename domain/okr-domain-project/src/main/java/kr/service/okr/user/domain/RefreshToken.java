package kr.service.okr.user.domain;

import static kr.service.okr.user.domain.AuthenticationProvider.*;
import static kr.service.okr.user.validator.Validator.*;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RefreshToken {
	private Long refreshTokenSeq;
	private final String userEmail;
	private String refreshToken;

	@Builder
	private RefreshToken(final Long refreshTokenSeq, final String userEmail, final String refreshToken) {
		if (refreshTokenSeq == null || userEmail == null || refreshToken == null)
			throw new OkrApplicationException(ErrorCode.INTERNAL_SERVER_ERROR);
		this.refreshTokenSeq = refreshTokenSeq;
		this.userEmail = userEmail;
		this.refreshToken = refreshToken;
	}

	private RefreshToken(final String userEmail, final String refreshToken) {
		validateEmail(userEmail);
		validateRefreshToken(refreshToken);
		this.userEmail = userEmail;
		this.refreshToken = refreshToken;
	}

	public static RefreshToken generateNewRefreshToken(final String userEmail) {
		return new RefreshToken(userEmail, generateRefreshToken(userEmail));
	}

	public RefreshToken checkAndRenewToken() {
		if (isTokenAboutToExpired(this.refreshToken)) {
			this.refreshToken = generateRefreshToken(this.userEmail);
		}
		return this;
	}
}