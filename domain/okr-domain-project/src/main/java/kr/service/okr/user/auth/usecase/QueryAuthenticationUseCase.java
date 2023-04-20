package kr.service.okr.user.auth.usecase;

import java.util.Optional;

public interface QueryAuthenticationUseCase {
	Optional<String> queryEmailBy(String token);

}
