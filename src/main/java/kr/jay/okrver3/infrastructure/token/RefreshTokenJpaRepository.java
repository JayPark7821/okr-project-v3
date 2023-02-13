package kr.jay.okrver3.infrastructure.token;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.jay.okrver3.domain.token.RefreshToken;
import kr.jay.okrver3.domain.token.RefreshTokenRepository;

public interface RefreshTokenJpaRepository extends RefreshTokenRepository,  JpaRepository<RefreshToken, Long> {

}
