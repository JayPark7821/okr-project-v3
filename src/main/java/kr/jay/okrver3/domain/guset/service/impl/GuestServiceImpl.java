package kr.jay.okrver3.domain.guset.service.impl;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.guset.service.GuestInfo;
import kr.jay.okrver3.domain.guset.service.GuestReader;
import kr.jay.okrver3.domain.guset.service.GuestService;
import kr.jay.okrver3.domain.guset.service.GuestStore;
import kr.jay.okrver3.infrastructure.user.auth.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GuestServiceImpl implements GuestService {

	private final GuestStore guestStore;
	private final GuestReader guestReader;

	@Transactional
	@Override
	public GuestInfo createNewGuestFrom(OAuth2UserInfo info) {
		guestReader.findByEmail(info.email()).ifPresent(guestStore::delete);
		return new GuestInfo(guestStore.save(info.toGuest()));
	}

	@Override
	public GuestInfo getGuestInfoFrom(String guestTempId) {
		return guestReader.findByGuestUuid(guestTempId)
			.map(GuestInfo::new)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_JOIN_INFO));
	}
}
