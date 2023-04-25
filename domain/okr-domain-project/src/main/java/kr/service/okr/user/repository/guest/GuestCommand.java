package kr.service.okr.user.repository.guest;

import kr.service.okr.user.domain.Guest;

public interface GuestCommand {
	void delete(Guest guest);

	Guest save(Guest guest);
}
