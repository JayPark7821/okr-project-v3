package kr.service.okr.persistence.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.service.okr.model.guset.ProviderType;
import kr.service.okr.model.user.JobField;
import kr.service.okr.model.user.RoleType;
import kr.service.okr.persistence.config.BaseEntity;
import kr.service.okr.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
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
	private RoleType roleType;
	private String password;

	@Enumerated(EnumType.STRING)
	private JobField jobField;

	@Builder
	public UserJpaEntity(Long userSeq, String userId, String username, String email, String profileImage,
		ProviderType providerType, RoleType roleType, String password, JobField jobField) {
		this.userSeq = userSeq;
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.profileImage = profileImage;
		this.providerType = providerType;
		this.roleType = roleType;
		this.password = password;
		this.jobField = jobField;
	}

	public void updateUserName(String username) {
		this.username = username != null ? username : this.username;
	}

	public void updateJobField(JobField jobField) {
		this.jobField = jobField != null ? jobField : this.jobField;
	}

	public User toDomain() {
		return new User(
			this.userSeq,
			this.userId,
			this.username,
			this.email,
			this.profileImage,
			this.providerType,
			this.roleType,
			this.password,
			this.jobField
		);
	}
}