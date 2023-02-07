package kr.jay.okrver3.infrastructure.guest;

import org.springframework.stereotype.Component;

import kr.jay.okrver3.domain.guset.service.GuestStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GuestStoreImpl implements GuestStore {

	private final GuestRepository guestRepository;

}
