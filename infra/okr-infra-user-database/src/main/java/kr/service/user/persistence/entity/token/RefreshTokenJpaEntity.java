package kr.service.user.persistence.entity.token;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
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

	public RefreshTokenJpaEntity(String userEmail, String refreshToken) {
		this.userEmail = userEmail;
		this.refreshToken = refreshToken;
	}

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
