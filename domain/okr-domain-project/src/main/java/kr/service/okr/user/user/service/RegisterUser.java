package kr.service.okr.user.user.service;

import org.springframework.stereotype.Service;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.user.guest.domain.Guest;
import kr.service.okr.user.guest.repository.GuestQuery;
import kr.service.okr.user.user.domain.User;
import kr.service.okr.user.user.repository.UserCommand;
import kr.service.okr.user.user.usecase.RegisterUserUseCase;
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
