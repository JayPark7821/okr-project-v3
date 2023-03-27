package kr.service.okr.domain.guset.service;

import kr.service.okr.domain.guset.Guest;

public interface GuestStore {
	Guest save(Guest guest);

	void delete(Guest guest);
}
