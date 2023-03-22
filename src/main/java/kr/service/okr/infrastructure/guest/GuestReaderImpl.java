package kr.service.okr.infrastructure.guest;

import java.util.Optional;

import org.springframework.stereotype.Component;

import kr.service.okr.domain.guset.Guest;
import kr.service.okr.domain.guset.service.GuestReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GuestReaderImpl implements GuestReader {

	private final GuestRepository guestRepository;

	@Override
	public Optional<Guest> findByEmail(String email) {
		return guestRepository.findByEmail(email);
	}

	@Override
	public Optional<Guest> findByGuestUuid(String guestTempId) {
		return guestRepository.findByGuestUuid(guestTempId);
	}
}
