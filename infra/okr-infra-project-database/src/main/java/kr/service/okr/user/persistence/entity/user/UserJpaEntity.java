package kr.service.okr.user.persistence.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.service.okr.config.BaseEntity;
import kr.service.okr.user.domain.User;
import kr.service.okr.user.enums.JobField;
import kr.service.okr.user.enums.ProviderType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_table")
public class UserJpaEntity extends BaseEntity {

	@Id
	@Column(name = "user_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userSeq;
	private String userId;
	private String username;
	private String email;
	private String profileImage;
	@Enumerated(EnumType.STRING)
	private ProviderType providerType;
	@Enumerated(EnumType.STRING)
	private JobField jobField;
	@Column(nullable = false)
	private boolean deleted = Boolean.FALSE;

	public UserJpaEntity(User user) {
		this.userId = user.getUserId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.profileImage = user.getProfileImage();
		this.providerType = user.getProviderType();
		this.jobField = user.getJobField();
	}

	public void updateUserName(String username) {
		this.username = username != null ? username : this.username;
	}

	public void updateJobField(JobField jobField) {
		this.jobField = jobField != null ? jobField : this.jobField;
	}

	public User toDomain() {
		return User.builder()
			.userSeq(this.userSeq)
			.userId(this.userId)
			.username(this.username)
			.email(this.email)
			.profileImage(this.profileImage)
			.providerType(this.providerType)
			.jobField(this.jobField)
			.build();
	}

}
