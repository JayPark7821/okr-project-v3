package kr.service.okr.application.user;

import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.service.jwt.JwtService;
import kr.service.oauth.platform.OAuth2UserInfo;
import kr.service.okr.user.enums.ProviderType;
import kr.service.okr.user.guest.usecase.JoinNewGuestUseCase;
import kr.service.okr.user.token.usecase.GenerateTokenSetUseCase;
import kr.service.okr.user.user.usecase.QueryUserUseCase;
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

}
