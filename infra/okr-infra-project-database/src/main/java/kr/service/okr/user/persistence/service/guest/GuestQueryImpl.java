package kr.service.okr.user.persistence.service.guest;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.user.domain.Guest;
import kr.service.okr.user.persistence.entity.guset.GuestJpaEntity;
import kr.service.okr.user.persistence.repository.guest.GuestRepository;
import kr.service.okr.user.repository.guest.GuestQuery;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GuestQueryImpl implements GuestQuery {

	private final GuestRepository repository;

	@Override
	public Optional<Guest> findByEmail(final String email) {
		return repository.findByEmail(email).map(GuestJpaEntity::toDomain);
	}

	@Override
	public Optional<Guest> findByGuestUuid(final String guestUuid) {
		return repository.findByGuestUuid(guestUuid).map(GuestJpaEntity::toDomain);
	}
}
