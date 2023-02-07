package kr.jay.okrver3.infrastructure.guest;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.jay.okrver3.domain.guset.Guest;

public interface GuestRepository extends JpaRepository<Guest, Long> {
}
