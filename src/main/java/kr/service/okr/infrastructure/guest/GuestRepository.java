package kr.service.okr.infrastructure.guest;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.service.okr.domain.guset.Guest;

public interface GuestRepository extends JpaRepository<Guest, Long> {
	Optional<Guest> findByEmail(String email);

	Optional<Guest> findByGuestUuid(String guestTempId);
}
