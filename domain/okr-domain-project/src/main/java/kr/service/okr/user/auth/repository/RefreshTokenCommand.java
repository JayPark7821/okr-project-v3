package kr.service.okr.user.auth.repository;

import kr.service.okr.user.auth.domain.RefreshToken;

public interface RefreshTokenCommand {
	RefreshToken save(RefreshToken refreshToken);
}
