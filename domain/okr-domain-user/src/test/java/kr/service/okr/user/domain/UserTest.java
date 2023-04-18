package kr.service.okr.user.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import kr.service.user.ProviderType;
import kr.service.user.exception.ErrorCode;
import kr.service.user.exception.OkrUserDomainException;
import kr.service.user.user.domain.JobField;
import kr.service.user.user.domain.RoleType;
import kr.service.user.user.domain.User;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserTest {

	@ParameterizedTest
	@MethodSource("CreateUserTestSource")
	void 유저_생성_실패_테스트_케이스(
		final String id,
		final String username,
		final String email,
		final String profileImageUrl,
		final ProviderType providerType,
		final JobField jobField,
		final String errorMsg
	) throws Exception {

		assertThatThrownBy(() -> new User(id, username, email, profileImageUrl, providerType, jobField))
			.isInstanceOf(OkrUserDomainException.class)
			.hasMessage(errorMsg);
	}

	@Test
	void 유저_생성_성공_테스트_케이스() throws Exception {
		final String id = "id";
		final String username = "username";
		final String email = "email@email.com";
		final ProviderType provider = ProviderType.GOOGLE;
		final String profileImage = "profileImage";
		final JobField jobField = JobField.WEB_SERVER_DEVELOPER;

		final User user = new User(id, username, email, profileImage, provider, jobField);

		assertThat(user.getUserId()).isEqualTo(id);
		assertThat(user.getUsername()).isEqualTo(username);
		assertThat(user.getEmail()).isEqualTo(email);
		assertThat(user.getProviderType()).isEqualTo(provider);
		assertThat(user.getProfileImage()).isEqualTo(profileImage);
		assertThat(user.getJobField()).isEqualTo(jobField);
		assertThat(user.getRoleType()).isEqualTo(RoleType.USER);
		assertThat(user.getPassword()).isNotNull();
	}

	private static Stream<Arguments> CreateUserTestSource() {
		final String id = "id";
		final String username = "username";
		final String email = "email@email.com";
		final ProviderType provider = ProviderType.GOOGLE;
		final String profileImage = "profileImage";
		final String longUsername = IntStream.range(0, 21)
			.mapToObj(i -> "a")
			.collect(Collectors.joining());
		final JobField jobField = JobField.WEB_SERVER_DEVELOPER;
		return Stream.of(
			Arguments.of(null, username, email, profileImage, provider, jobField,
				ErrorCode.ID_IS_REQUIRED.getMessage()),
			Arguments.of(id, null, email, profileImage, provider, jobField,
				ErrorCode.USERNAME_IS_REQUIRED.getMessage()),
			Arguments.of(id, longUsername, email, profileImage, provider, jobField,
				ErrorCode.USERNAME_IS_REQUIRED.getMessage()),
			Arguments.of(id, username, null, profileImage, provider, jobField,
				ErrorCode.EMAIL_IS_REQUIRED.getMessage()),
			Arguments.of(id, username, email, profileImage, null, jobField,
				ErrorCode.PROVIDER_TYPE_IS_REQUIRED.getMessage()),
			Arguments.of(id, username, email, profileImage, provider, null,
				ErrorCode.JOB_FIELD_IS_REQUIRED.getMessage())
		);
	}

	@Test
	void 가입한_소셜플랫폼과_다른_플랫폼() throws Exception {
		final User testUser = User.builder()
			.userSeq(1L)
			.userId("test")
			.email("test@email.com")
			.providerType(ProviderType.GOOGLE)
			.roleType(RoleType.USER)
			.username("testUser")
			.profileImage("testImage")
			.password("testPassword")
			.jobField(JobField.AI_DEVELOPER)
			.build();

		Assertions.assertThatThrownBy(() -> testUser.validateProvider(ProviderType.APPLE))
			.isInstanceOf(OkrUserDomainException.class)
			.hasMessage(ErrorCode.MISS_MATCH_PROVIDER.getMessage().formatted(ProviderType.GOOGLE.name()));

	}
}