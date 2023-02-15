package kr.jay.okrver3.domain.user;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_table")
public class User implements UserDetails {

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

	@Builder
	public User(Long userSeq, String userId, String username, String email, String profileImage,
		ProviderType providerType, RoleType roleType, String password) {
		this.userSeq = userSeq;
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.profileImage = profileImage;
		this.providerType = providerType;
		this.roleType = roleType;
		this.password = password;
	}

	public void validateProvider(ProviderType providerType) {
		if (this.providerType != providerType) {
			throw new IllegalArgumentException(this.providerType.getName() + "(으)로 가입한 계정이 있습니다.");
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(this.getRoleType().getValue()));
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		User user = (User)o;
		return Objects.equals(userSeq, user.userSeq) && Objects.equals(userId, user.userId)
			&& Objects.equals(username, user.username) && Objects.equals(email, user.email)
			&& Objects.equals(profileImage, user.profileImage) && providerType == user.providerType
			&& roleType == user.roleType && Objects.equals(password, user.password);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userSeq, userId, username, email, profileImage, providerType, roleType, password);
	}

}
