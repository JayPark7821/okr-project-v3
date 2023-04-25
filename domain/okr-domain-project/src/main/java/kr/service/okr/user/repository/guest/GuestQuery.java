package kr.service.okr.user.repository.guest;

import java.util.Optional;

import kr.service.okr.user.domain.Guest;

public interface GuestQuery {
	Optional<Guest> findByEmail(String email);

	Optional<Guest> findByGuestUuid(String guestUuid);
}
