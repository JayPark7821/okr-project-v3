package kr.service.okr.user.service.guest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.user.domain.Guest;
import kr.service.okr.user.repository.guest.GuestCommand;
import kr.service.okr.user.repository.guest.GuestQuery;
import kr.service.okr.user.usecase.guest.RegisterGuestUseCase;
import kr.service.okr.user.usecase.user.LoginInfo;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterGuest implements RegisterGuestUseCase {

	private final GuestQuery guestQuery;
	private final GuestCommand guestCommand;

	@Override
	public LoginInfo command(final Command command) {
		guestQuery.findByEmail(command.email())
			.ifPresent(guestCommand::delete);

		return new LoginInfo(
			guestCommand.save(toGuest(command))
		);
	}

	private Guest toGuest(final Command command) {
		return new Guest(
			command.id(),
			command.username(),
			command.email(),
			command.providerType(),
			command.profileImageUrl()
		);
	}
}
