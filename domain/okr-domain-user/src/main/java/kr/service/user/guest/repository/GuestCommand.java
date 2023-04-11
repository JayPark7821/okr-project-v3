package kr.service.user.guest.repository;

import kr.service.user.guest.domain.Guest;

public interface GuestCommand {
	void delete(Guest guest);

	Guest save(Guest guest);
}
