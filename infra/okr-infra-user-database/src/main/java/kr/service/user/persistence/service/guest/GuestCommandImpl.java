package kr.service.user.persistence.service.guest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.user.guest.domain.Guest;
import kr.service.user.guest.repository.GuestCommand;
import kr.service.user.persistence.entity.guset.GuestJpaEntity;
import kr.service.user.persistence.repository.guest.GuestRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class GuestCommandImpl implements GuestCommand {

	private final GuestRepository repository;

	@Override
	public void delete(final Guest guest) {
		repository.delete(new GuestJpaEntity(guest));
	}

	@Override
	public Guest save(final Guest guest) {
		return repository.save(new GuestJpaEntity(guest)).toDomain();
	}
}
