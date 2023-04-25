package kr.service.okr.user.service.token;

import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.service.okr.user.repository.token.AuthenticationRepository;
import kr.service.okr.user.usecase.token.QueryAuthenticationUseCase;
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
