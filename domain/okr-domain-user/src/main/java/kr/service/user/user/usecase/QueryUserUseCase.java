package kr.service.user.user.usecase;

import java.util.Optional;

import kr.service.user.user.domain.User;

public interface QueryUserUseCase {

	Optional<User> query(String email);

}
