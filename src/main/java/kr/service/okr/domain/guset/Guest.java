package kr.service.okr.domain.guset;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import kr.service.okr.common.utils.TokenGenerator;
import kr.service.okr.domain.user.ProviderType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "guest")
public class Guest {

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
	public Guest(String guestId, String guestName, String email, ProviderType providerType, String profileImage) {
		this.guestUuid = TokenGenerator.randomCharacterWithPrefix(GUEST_PREFIX);
		this.guestId = guestId;
		this.guestName = guestName;
		this.email = email;
		this.providerType = providerType;
		this.profileImage = profileImage;
	}
}
