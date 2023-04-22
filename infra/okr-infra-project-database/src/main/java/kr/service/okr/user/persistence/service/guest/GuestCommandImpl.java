package kr.service.okr.user.persistence.service.guest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.user.guest.domain.Guest;
import kr.service.okr.user.guest.repository.GuestCommand;
import kr.service.okr.user.persistence.entity.guset.GuestJpaEntity;
import kr.service.okr.user.persistence.repository.guest.GuestRepository;
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