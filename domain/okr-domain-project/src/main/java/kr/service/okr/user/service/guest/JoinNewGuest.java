package kr.service.okr.user.service.guest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.user.domain.Guest;
import kr.service.okr.user.repository.guest.GuestCommand;
import kr.service.okr.user.repository.guest.GuestQuery;
import kr.service.okr.user.usecase.guest.JoinNewGuestUseCase;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class JoinNewGuest implements JoinNewGuestUseCase {

	private final GuestQuery guestQuery;
	private final GuestCommand guestCommand;

	@Override
	public Guest command(final Command command) {
		guestQuery.findByEmail(command.email()).ifPresent(guestCommand::delete);
		return guestCommand.save(
			new Guest(
				command.id(),
				command.username(),
				command.email(),
				command.providerType(),
				command.profileImageUrl()
			)
		);
	}
}
