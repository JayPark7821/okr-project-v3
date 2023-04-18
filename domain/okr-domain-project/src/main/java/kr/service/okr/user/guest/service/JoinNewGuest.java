package kr.service.okr.user.guest.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.user.guest.domain.Guest;
import kr.service.okr.user.guest.repository.GuestCommand;
import kr.service.okr.user.guest.repository.GuestQuery;
import kr.service.okr.user.guest.usecase.JoinNewGuestUseCase;
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
