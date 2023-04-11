package kr.service.user.guest.repository;

import java.util.Optional;

import kr.service.user.guest.domain.Guest;

public interface GuestQuery {
	Optional<Guest> findByEmail(String email);
}
