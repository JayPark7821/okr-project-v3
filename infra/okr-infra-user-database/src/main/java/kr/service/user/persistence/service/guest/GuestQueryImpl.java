package kr.service.user.persistence.service.guest;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.user.guest.domain.Guest;
import kr.service.user.guest.repository.GuestQuery;
import kr.service.user.persistence.entity.guset.GuestJpaEntity;
import kr.service.user.persistence.repository.guest.GuestRepository;
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
}
