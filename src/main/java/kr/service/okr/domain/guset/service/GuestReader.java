package kr.service.okr.domain.guset.service;

import java.util.Optional;

import kr.service.okr.domain.guset.Guest;

public interface GuestReader {
	Optional<Guest> findByEmail(String email);

	Optional<Guest> findByGuestUuid(String guestTempId);
}
