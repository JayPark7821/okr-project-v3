package kr.service.okr.user.persistence.repository.guest;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.service.okr.user.persistence.entity.guset.GuestJpaEntity;

public interface GuestRepository extends JpaRepository<GuestJpaEntity, Long> {
	Optional<GuestJpaEntity> findByEmail(String email);

	Optional<GuestJpaEntity> findByGuestUuid(String guestTempId);
}
