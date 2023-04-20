package kr.service.okr.user.auth.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.service.okr.user.auth.repository.AuthenticationRepository;
import kr.service.okr.user.auth.usecase.QueryAuthenticationUseCase;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QueryAuthentication implements QueryAuthenticationUseCase {

	private final AuthenticationRepository repository;

	@Override
	public Optional<String> queryEmailBy(final String token) {
		return repository.findEmailByToken(token);

	}

}
