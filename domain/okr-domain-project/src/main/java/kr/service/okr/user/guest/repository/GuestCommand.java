package kr.service.okr.user.guest.repository;

import kr.service.okr.user.guest.domain.Guest;

public interface GuestCommand {
	void delete(Guest guest);

	Guest save(Guest guest);
}
