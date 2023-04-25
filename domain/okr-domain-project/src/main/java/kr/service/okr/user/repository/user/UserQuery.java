package kr.service.okr.user.repository.user;

import java.util.List;
import java.util.Optional;

import kr.service.okr.user.domain.User;

public interface UserQuery {

	Optional<User> findByEmail(String email);

	List<Long> findUserSeqsByEmails(List<String> emails);
}
