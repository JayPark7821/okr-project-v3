package kr.service.user.user.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.service.user.user.domain.User;
import kr.service.user.user.repository.UserQuery;
import kr.service.user.user.usecase.QueryUserUseCase;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QueryUser implements QueryUserUseCase {

	private final UserQuery userQuery;

	@Override
	public Optional<User> query(final String email) {
		return userQuery.findByEmail(email);
	}
}
