package kr.service.okr.user.token.repository;

import kr.service.okr.user.token.domain.RefreshToken;

public interface RefreshTokenCommand {
	RefreshToken save(RefreshToken refreshToken);
}
