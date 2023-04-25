package kr.service.okr.user.repository.token;

import kr.service.okr.user.domain.RefreshToken;

public interface RefreshTokenCommand {
	RefreshToken save(RefreshToken refreshToken);
}
