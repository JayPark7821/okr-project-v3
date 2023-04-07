package kr.service.okr.persistence.service.guest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.service.okr.persistence.entity.guset.GuestJpaEntity;
import kr.service.okr.persistence.repository.guest.GuestRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class GuestCommand {

	private final GuestRepository guestRepository;

	public GuestJpaEntity save(GuestJpaEntity guest) {
		return guestRepository.save(guest);
	}

	public void delete(GuestJpaEntity guest) {
		guestRepository.delete(guest);
	}

}
