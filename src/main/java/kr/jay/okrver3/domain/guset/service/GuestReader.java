package kr.jay.okrver3.domain.guset.service;

import java.util.Optional;

import kr.jay.okrver3.domain.guset.Guest;

public interface GuestReader {
	Optional<Guest> findByEmail(String email);

	Optional<Guest> findByGuestUuid(String guestTempId);
}
