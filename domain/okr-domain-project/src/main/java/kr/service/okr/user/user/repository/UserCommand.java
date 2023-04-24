package kr.service.okr.user.user.repository;

import kr.service.okr.user.user.domain.User;

public interface UserCommand {

	User save(User user);
}
