package kr.service.user.application.user;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.ExpiredJwtException;
import kr.service.jwt.JwtService;
import kr.service.oauth.platform.OAuth2UserInfo;
import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.user.ProviderType;
import kr.service.user.guest.usecase.JoinNewGuestUseCase;
import kr.service.user.token.usecase.GenerateTokenSetUseCase;
import kr.service.user.user.domain.User;
import kr.service.user.user.usecase.QueryUserUseCase;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserFacade {

	private final QueryUserUseCase queryUserUseCase;
	private final GenerateTokenSetUseCase GenerateTokenSetUseCase;
	private final JoinNewGuestUseCase joinNewGuestUseCase;
	private final JwtService jwtService;

	public Optional<LoginInfo> getLoginInfoFrom(final OAuth2UserInfo info) {
		return queryUserUseCase.query(info.email())
			.map(user -> new LoginInfo(user, GenerateTokenSetUseCase.command(user.getEmail())));
	}

	public LoginInfo createGuest(final OAuth2UserInfo info) {
		return new LoginInfo(joinNewGuestUseCase.command(createCommand(info)));
	}

	private JoinNewGuestUseCase.Command createCommand(final OAuth2UserInfo info) {
		return new JoinNewGuestUseCase.Command(
			info.id(),
			info.username(),
			info.email(),
			info.profileImage(),
			ProviderType.of(info.socialPlatform())
		);
	}

	public User getUserInfoBy(final String jwt) {
		try {
			final String email = jwtService.getEmail(jwt);
			return queryUserUseCase.query(email)
				.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_TOKEN));
		} catch (ExpiredJwtException e) {
			throw new OkrApplicationException(ErrorCode.EXPIRED_TOKEN);
		} catch (Exception e) {
			throw new OkrApplicationException(ErrorCode.INVALID_TOKEN);
		}
	}

	public List<Long> getUserSeqsBy(final List<String> userEmails) {
		return queryUserUseCase.query(userEmails);
	}
}
