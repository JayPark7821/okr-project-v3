package kr.service.user.user.repository;

import java.util.List;
import java.util.Optional;

import kr.service.user.user.domain.User;

public interface UserQuery {

	Optional<User> findByEmail(String email);

	List<Long> findUserSeqsByEmails(List<String> emails);
}
