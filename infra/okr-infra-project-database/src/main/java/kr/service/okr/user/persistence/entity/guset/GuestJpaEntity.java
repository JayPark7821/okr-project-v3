package kr.service.okr.user.persistence.entity.guset;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.service.okr.config.BaseTimeEntity;
import kr.service.okr.user.domain.Guest;
import kr.service.okr.user.enums.ProviderType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "guest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GuestJpaEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long guestSeq;

	private String guestUuid;

	private String guestId;

	private String guestName;

	private String email;

	@Enumerated(EnumType.STRING)
	private ProviderType providerType;

	private String profileImage;

	public GuestJpaEntity(final Guest guest) {
		this.guestUuid = guest.getGuestUuid();
		this.guestId = guest.getGuestId();
		this.guestName = guest.getGuestName();
		this.email = guest.getEmail();
		this.providerType = guest.getProviderType();
		this.profileImage = guest.getProfileImage();
	}

	public Guest toDomain() {
		return Guest.builder()
			.guestSeq(guestSeq)
			.guestUuid(guestUuid)
			.guestId(guestId)
			.guestName(guestName)
			.email(email)
			.providerType(providerType)
			.profileImage(profileImage)
			.build();

	}
}
