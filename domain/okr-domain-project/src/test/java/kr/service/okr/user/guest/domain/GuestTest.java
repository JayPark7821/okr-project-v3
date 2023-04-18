package kr.service.okr.user.guest.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import kr.service.user.ProviderType;

class GuestTest {

	@ParameterizedTest
	@MethodSource("CreateGuestTestSource")
	void 게스트_생성_실패_테스트_케이스(
		final String id,
		final String username,
		final String email,
		final ProviderType providerType,
		final String profileImageUrl,
		final String errorMsg
	) throws Exception {

		assertThatThrownBy(() -> new Guest(id, username, email, providerType, profileImageUrl))
			.isInstanceOf(OkrUserDomainException.class)
			.hasMessage(errorMsg);

	}

	@Test
	void 게스트_생성_성공_테스트_케이스() throws Exception {
		final String id = "id";
		final String username = "username";
		final String email = "email@email.com";
		final ProviderType provider = ProviderType.GOOGLE;
		final String profileImage = "profileImage";

		final Guest guest = new Guest(id, username, email, provider, profileImage);

		assertThat(guest.getGuestUuid()).containsPattern(Pattern.compile("guest-[a-zA-Z0-9]{14}"));
		assertThat(guest.getGuestId()).isEqualTo(id);
		assertThat(guest.getGuestName()).isEqualTo(username);
		assertThat(guest.getEmail()).isEqualTo(email);
		assertThat(guest.getProviderType()).isEqualTo(provider);
		assertThat(guest.getProfileImage()).isEqualTo(profileImage);

	}

	private static Stream<Arguments> CreateGuestTestSource() {
		final String id = "id";
		final String username = "username";
		final String email = "email@email.com";
		final ProviderType provider = ProviderType.GOOGLE;
		final String profileImage = "profileImage";
		final String longUsername = IntStream.range(0, 21)
			.mapToObj(i -> "a")
			.collect(Collectors.joining());
		return Stream.of(
			Arguments.of(null, username, email, provider, profileImage, ErrorCode.ID_IS_REQUIRED.getMessage()),
			Arguments.of(id, null, email, provider, profileImage, ErrorCode.USERNAME_IS_REQUIRED.getMessage()),
			Arguments.of(id, longUsername, email, provider, profileImage, ErrorCode.USERNAME_IS_REQUIRED.getMessage()),
			Arguments.of(id, username, null, provider, profileImage, ErrorCode.EMAIL_IS_REQUIRED.getMessage()),
			Arguments.of(id, username, email, null, profileImage, ErrorCode.PROVIDER_TYPE_IS_REQUIRED.getMessage())
		);
	}
}