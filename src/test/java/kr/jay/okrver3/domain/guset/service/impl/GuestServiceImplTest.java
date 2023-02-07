package kr.jay.okrver3.domain.guset.service.impl;


import java.util.regex.Pattern;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import kr.jay.okrver3.domain.guset.service.GuestInfo;
import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.infrastructure.guest.GuestStoreImpl;
import kr.jay.okrver3.interfaces.user.OAuth2UserInfo;

@DataJpaTest
@Import({GuestServiceImpl.class, GuestStoreImpl.class})
class GuestServiceImplTest {

	@Autowired
	private GuestServiceImpl sut;

	@Test
	@DisplayName("가입한 유저 정보가 없을때 소셜 idToken을 통해 로그인을 시도하면 기대하는 응답(Guest)을 반환한다.")
	void create_new_guest_from_oauth2info () throws Exception {
		String id = "googleId";
		String userName = "userName";
		String email = "google@gmail.com";
		String pictureUrl = "pictureUrl";
		ProviderType google = ProviderType.GOOGLE;

		OAuth2UserInfo info =
			new OAuth2UserInfo(id, userName, email, pictureUrl, google);

		GuestInfo guestInfo = sut.createNewGuestFrom(info);

		assertGuestInfo(guestInfo, info);
	}

	private void assertGuestInfo(GuestInfo actual, OAuth2UserInfo expected){
		Assertions.assertThat(actual.guestUuid()).containsPattern(
			Pattern.compile("guest-[a-zA-Z0-9]{14}"));
		Assertions.assertThat(actual.name()).isEqualTo(expected.name());
		Assertions.assertThat(actual.email()).isEqualTo(expected.email());
		Assertions.assertThat(actual.profileImageUrl()).isEqualTo(expected.picture());
		Assertions.assertThat(actual.providerType()).isEqualTo(expected.providerType());
	}
}