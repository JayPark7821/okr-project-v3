package kr.service.okr.user.service.user;

import org.springframework.stereotype.Service;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.user.domain.Guest;
import kr.service.okr.user.domain.RefreshToken;
import kr.service.okr.user.domain.User;
import kr.service.okr.user.repository.guest.GuestQuery;
import kr.service.okr.user.repository.token.AuthenticationRepository;
import kr.service.okr.user.repository.token.RefreshTokenCommand;
import kr.service.okr.user.repository.user.UserCommand;
import kr.service.okr.user.usecase.user.LoginInfo;
import kr.service.okr.user.usecase.user.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterUser implements RegisterUserUseCase {

	private final GuestQuery guestQuery;
	private final UserCommand userCommand;
	private final RefreshTokenCommand refreshTokenCommand;
	private final AuthenticationRepository authenticationRepository;

	@Override
	public LoginInfo command(final Command command) {
		return new LoginInfo(
			userCommand.save(createUser(command)),
			authenticationRepository.generateAccessToken(command.email()),
			getRefreshToken(command)
		);
	}

	private String getRefreshToken(final Command command) {
		return refreshTokenCommand.save(
			new RefreshToken(
				command.email(),
				authenticationRepository.generateRefreshToken(command.email())
			)
		).getRefreshToken();
	}

	private User createUser(final Command command) {
		return User.registerUserFrom(
			queryGuestInfoFrom(command),
			command.username(),
			command.email(),
			command.jobField()
		);
	}

	private Guest queryGuestInfoFrom(final Command command) {
		return guestQuery.findByGuestUuid(command.guestUuid())
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_JOIN_INFO));
	}
}
