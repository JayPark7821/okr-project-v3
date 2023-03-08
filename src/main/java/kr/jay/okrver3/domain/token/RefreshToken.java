package kr.jay.okrver3.domain.token;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "refresh_token", indexes = @Index(name = "idx_user_email", columnList = "userEmail"))
public class RefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long refreshTokenSeq;

	private String userEmail;

	private String refreshToken;

	public RefreshToken(String userEmail, String refreshToken) {
		this.userEmail = userEmail;
		this.refreshToken = refreshToken;
	}

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
