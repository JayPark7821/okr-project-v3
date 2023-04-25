package kr.service.okr.user.repository.user;

import kr.service.okr.user.domain.User;

public interface UserCommand {

	User save(User user);
}
