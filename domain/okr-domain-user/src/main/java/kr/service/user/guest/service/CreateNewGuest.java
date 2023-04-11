package kr.service.user.guest.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.user.guest.domain.Guest;
import kr.service.user.guest.repository.GuestCommand;
import kr.service.user.guest.repository.GuestQuery;
import kr.service.user.guest.usecase.CreateNewGuestUseCase;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateNewGuest implements CreateNewGuestUseCase {

	private final GuestQuery guestQuery;
	private final GuestCommand guestCommand;

	@Override
	public Guest createNewGuestFrom(final Command command) {
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
