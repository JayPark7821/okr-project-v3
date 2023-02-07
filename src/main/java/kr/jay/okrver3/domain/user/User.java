package kr.jay.okrver3.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "user_table")
public class User {

	@Id
	@Column(name = "user_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userSeq;
	private String userId;
	private String username;
	private String email;
	private String profileImage;
	private ProviderType providerType;

	@Builder
	public User(Long userSeq, String userId, String username, String email, String profileImage, ProviderType providerType) {
		this.userSeq = userSeq;
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.profileImage = profileImage;
		this.providerType = providerType;
	}

	public void validateProvider(ProviderType providerType) {
		if (this.providerType != providerType) {
			throw new IllegalArgumentException(this.providerType.getName() + "(으)로 가입한 계정이 있습니다.");
		}
	}
}
