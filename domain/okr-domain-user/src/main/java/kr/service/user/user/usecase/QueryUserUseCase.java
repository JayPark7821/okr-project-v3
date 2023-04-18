package kr.service.user.user.usecase;

import java.util.List;
import java.util.Optional;

import kr.service.user.user.domain.User;

public interface QueryUserUseCase {

	Optional<User> query(String email);

	List<Long> query(List<String> emails);
}
