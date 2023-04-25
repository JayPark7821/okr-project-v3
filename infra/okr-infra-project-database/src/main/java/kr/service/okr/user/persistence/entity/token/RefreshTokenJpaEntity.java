package kr.service.okr.user.persistence.entity.token;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import kr.service.okr.user.domain.RefreshToken;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "refresh_token", indexes = @Index(name = "idx_user_email", columnList = "userEmail"))
public class RefreshTokenJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long refreshTokenSeq;

	private String userEmail;

	private String refreshToken;

	public RefreshTokenJpaEntity(final RefreshToken refreshToken) {
		this.refreshTokenSeq = refreshToken.getRefreshTokenSeq();
		this.userEmail = refreshToken.getUserEmail();
		this.refreshToken = refreshToken.getRefreshToken();
	}

	public RefreshToken toDomain() {
		return RefreshToken.builder()
			.refreshTokenSeq(this.refreshTokenSeq)
			.userEmail(this.userEmail)
			.refreshToken(this.refreshToken)
			.build();
	}
}
