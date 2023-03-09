package kr.jay.okrver3.domain.token.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import kr.jay.okrver3.common.utils.JwtTokenUtils;
import kr.jay.okrver3.domain.token.RefreshToken;
import kr.jay.okrver3.domain.token.service.AuthTokenInfo;
import kr.jay.okrver3.domain.token.service.RefreshTokenRepository;
import kr.jay.okrver3.domain.user.JobFieldDetail;
import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.service.UserInfo;
import kr.jay.okrver3.infrastructure.token.RefreshTokenRepositoryImpl;

@DataJpaTest
@Import({TokenServiceImpl.class, RefreshTokenRepositoryImpl.class})
class TokenServiceImplTest {

	@PersistenceContext
	EntityManager em;

	@Value("${app.auth.tokenSecret}")
	private String key;
	@Autowired
	private TokenServiceImpl sut;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;


	@Test
	@DisplayName("email이 입력 되었을때 기대하는 응답(AuthTokenInfo)을 반환한다.")
	void create_new_authTokenInfo() throws Exception {
		UserInfo userInfo =
			new UserInfo(999L, "appleId", "appleUser", "apple@apple.com", "appleProfileImage", ProviderType.APPLE ,
				JobFieldDetail.WEB_SERVER_DEVELOPER);

		AuthTokenInfo authTokenInfo = sut.generateTokenSet(userInfo);

		assertThat(authTokenInfo.accessToken()).isNotNull();
		assertThat(authTokenInfo.refreshToken()).isNotNull();
	}

	@Test
	@DisplayName("남은 기간이 3일 이상인 refreshToken이 있을때 기존 refreshToken을 반환한다.")
	void returns_old_refreshToken_when_expire_date_more_then_3days() throws Exception {
		//given
		UserInfo userInfo =
			new UserInfo(999L, "appleId", "appleUser", "apple@apple.com", "appleProfileImage", ProviderType.APPLE ,
				JobFieldDetail.WEB_SERVER_DEVELOPER);
		String UsableRefreshToken = JwtTokenUtils.generateToken("apple@apple.com", key, 259300000L);
		refreshTokenRepository.save(new RefreshToken("apple@apple.com", UsableRefreshToken));

		//when
		AuthTokenInfo authTokenInfo = sut.generateTokenSet(userInfo);

		//then
		assertThat(authTokenInfo.accessToken()).isNotNull();
		assertThat(authTokenInfo.refreshToken()).isEqualTo(UsableRefreshToken);
	}

	@Test
	@DisplayName("남은 기간이 3일 미만인 refreshToken이 있을때 새로운 refreshToken을 반환한다.")
	void returns_new_refreshToken_when_expire_date_less_then_3days() throws Exception {
		//given
		UserInfo userInfo =
			new UserInfo(998L, "googleId", "googleUser", "google@google.com", "googleProfileImage", ProviderType.GOOGLE ,
				JobFieldDetail.WEB_SERVER_DEVELOPER);
		String UsableRefreshToken = JwtTokenUtils.generateToken(userInfo.email(), key, 100000L);
		refreshTokenRepository.save(new RefreshToken("google@google.com", UsableRefreshToken));

		//when
		AuthTokenInfo authTokenInfo = sut.generateTokenSet(userInfo);

		//then
		assertThat(authTokenInfo.accessToken()).isNotNull();
		assertThat(authTokenInfo.refreshToken()).isNotEqualTo(UsableRefreshToken);
	}


	@Test
	void refreshToken으로_getNewAccessToken을_호출하면_기대하는_응답을_리턴한다_new_accessToken() {

		String accessToken = JwtTokenUtils.generateToken("apple@apple.com", key, 10000000000000L);
		em.persist(new RefreshToken("apple@apple.com",accessToken ));
		AuthTokenInfo info = sut.getNewAccessToken(accessToken);

		assertThat(info.accessToken()).isNotNull();
		assertThat(info.refreshToken()).isEqualTo(accessToken);

	}


}