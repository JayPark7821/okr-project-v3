package kr.service.okr.user.service.user;

import org.springframework.stereotype.Service;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.user.domain.Guest;
import kr.service.okr.user.domain.User;
import kr.service.okr.user.repository.guest.GuestQuery;
import kr.service.okr.user.repository.user.UserCommand;
import kr.service.okr.user.usecase.user.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterUser implements RegisterUserUseCase {

	private final GuestQuery guestQuery;
	private final UserCommand userCommand;

	@Override
	public User command(final Command command) {

		Guest guest = guestQuery.findByGuestUuid(command.guestUuid())
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_JOIN_INFO));

		return userCommand.save(User.joinNewUser(guest, command.username(), command.email(), command.jobField()));
	}
}
