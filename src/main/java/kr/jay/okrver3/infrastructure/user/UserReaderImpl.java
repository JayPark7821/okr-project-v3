package kr.jay.okrver3.infrastructure.user;

import java.util.Optional;

import org.springframework.stereotype.Component;

import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.domain.user.service.UserReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserReaderImpl implements UserReader {

	private final UserRepository userRepository;

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}
