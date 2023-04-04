package kr.service.persistence.repository.guest;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.service.persistence.entity.guset.GuestJpaEntity;

public interface GuestRepository extends JpaRepository<GuestJpaEntity, Long> {
	Optional<GuestJpaEntity> findByEmail(String email);

	Optional<GuestJpaEntity> findByGuestUuid(String guestTempId);
}
