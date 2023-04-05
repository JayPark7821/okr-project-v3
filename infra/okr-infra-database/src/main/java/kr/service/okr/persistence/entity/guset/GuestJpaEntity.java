package kr.service.okr.persistence.entity.guset;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.service.okr.model.guset.ProviderType;
import kr.service.okr.persistence.config.BaseTimeEntity;
import kr.service.okr.util.TokenGenerator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "guest")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GuestJpaEntity extends BaseTimeEntity {

	private static final String GUEST_PREFIX = "guest-";

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

	@Builder
	private GuestJpaEntity(String guestId, String guestName, String email, ProviderType providerType,
		String profileImage) {
		this.guestUuid = TokenGenerator.randomCharacterWithPrefix(GUEST_PREFIX);
		this.guestId = guestId;
		this.guestName = guestName;
		this.email = email;
		this.providerType = providerType;
		this.profileImage = profileImage;
	}
}
