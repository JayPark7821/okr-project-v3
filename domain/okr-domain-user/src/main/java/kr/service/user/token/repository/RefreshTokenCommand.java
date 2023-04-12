package kr.service.user.token.repository;

import kr.service.user.token.domain.RefreshToken;

public interface RefreshTokenCommand {
	RefreshToken save(RefreshToken refreshToken);
}
