package kr.jay.okrver3.domain.guset.service.impl;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;
import java.util.regex.Pattern;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import kr.jay.okrver3.OAuth2UserInfoFixture;
import kr.jay.okrver3.domain.guset.service.GuestInfo;
import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.infrastructure.guest.GuestReaderImpl;
import kr.jay.okrver3.infrastructure.guest.GuestStoreImpl;
import kr.jay.okrver3.infrastructure.user.auth.OAuth2UserInfo;

@DataJpaTest
@Import({GuestServiceImpl.class, GuestStoreImpl.class, GuestReaderImpl.class})
class GuestServiceImplTest {

	@Autowired
	private GuestServiceImpl sut;

	@Test
	@DisplayName("가입한 유저 정보가 없을때 소셜 idToken을 통해 로그인을 시도하면 기대하는 응답(Guest)을 반환한다.")
	void create_new_guest_from_oauth2info() throws Exception {
		OAuth2UserInfo info = OAuth2UserInfoFixture.GoogleUserInfoFixture.build();

		GuestInfo guestInfo = sut.createNewGuestFrom(info);

		assertGuestInfo(guestInfo, info);
	}

	@Test
	@Sql("classpath:insert-guest.sql")
	@DisplayName("가입을 시도한적이 있는 유저가 소셜 idToken을 통해 로그인을 시도하면 기대하는 응답(새로운 Guest)을 반환한다.")
	void delete_old_guest_and_create_new_guest_from_oauth2info() throws Exception {
		String id = "testId";
		String userName = "testUser";
		String email = "test@email.com";
		String pictureUrl = "pic";
		ProviderType google = ProviderType.GOOGLE;

		OAuth2UserInfo info =
			new OAuth2UserInfo(id, userName, email, pictureUrl, google);

		GuestInfo guestInfo = sut.createNewGuestFrom(info);

		assertGuestInfo(guestInfo, info);
	}

	@Test
	@DisplayName("가입을 시도한적이 없는 유저가 getGuestInfoBy()을 호출하면 기대하는 응답(Optional.empty())을 반환한다.")
	void throw_exception_when_no_guest_request_join() throws Exception {

		String guestTempId = "not-registered-guestUuid";
		assertThat(sut.getGuestInfoFrom(guestTempId)).isEmpty();
	}

	@Test
	@Sql("classpath:insert-guest.sql")
	@DisplayName("가입을 시도한적이 있는 유저가 getGuestInfoBy()을 호출하면 기대하는 응답(GusetInfo)을 반환한다.")
	void return_guest_info_when_guest_request() throws Exception {
		String guestTempId = "registered-guestUuid";
		String id = "testId";
		String userName = "testUser";
		String email = "test@email.com";
		String pictureUrl = "pic";
		ProviderType google = ProviderType.GOOGLE;

		OAuth2UserInfo info =
			new OAuth2UserInfo(id, userName, email, pictureUrl, google);

		Optional<GuestInfo> guestInfo = sut.getGuestInfoFrom(guestTempId);

		assertGuestInfo(guestInfo.get(), info);
	}

	private void assertGuestInfo(GuestInfo actual, OAuth2UserInfo expected) {
		Assertions.assertThat(actual.guestUuid()).containsPattern(
			Pattern.compile("guest-[a-zA-Z0-9]{14}"));
		Assertions.assertThat(actual.name()).isEqualTo(expected.name());
		Assertions.assertThat(actual.email()).isEqualTo(expected.email());
		Assertions.assertThat(actual.profileImageUrl()).isEqualTo(expected.picture());
		Assertions.assertThat(actual.providerType()).isEqualTo(expected.providerType());
	}
}