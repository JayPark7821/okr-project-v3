package kr.jay.okrver3.domain.user.service;

import java.util.Optional;

import kr.jay.okrver3.domain.user.User;

public interface UserReader {
	Optional<User> findByEmail(String email);
}
