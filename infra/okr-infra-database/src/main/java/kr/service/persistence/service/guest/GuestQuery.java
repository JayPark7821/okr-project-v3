package kr.service.persistence.service.guest;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.persistence.entity.guset.GuestJpaEntity;
import kr.service.persistence.repository.guest.GuestRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GuestQuery {

	private final GuestRepository guestRepository;

	public Optional<GuestJpaEntity> findByEmail(String email) {
		return guestRepository.findByEmail(email);
	}

	public Optional<GuestJpaEntity> findByGuestUuid(String guestTempId) {
		return guestRepository.findByGuestUuid(guestTempId);
	}
}
