package kr.jay.okrver3.infrastructure.guest;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.jay.okrver3.domain.guset.Guest;

public interface GuestRepository extends JpaRepository<Guest, Long> {
	Optional<Guest> findByEmail(String email);
}
