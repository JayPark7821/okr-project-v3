package kr.service.okr.user.guest.repository;

import java.util.Optional;

import kr.service.okr.user.guest.domain.Guest;

public interface GuestQuery {
	Optional<Guest> findByEmail(String email);
}
