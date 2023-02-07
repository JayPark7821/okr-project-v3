package kr.jay.okrver3.domain.guset;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kr.jay.okrver3.common.utils.TokenGenerator;
import kr.jay.okrver3.domain.user.ProviderType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "guest")
public class Guest {

	private static final String GUEST_PREFIX = "guest_";

	@Id
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
