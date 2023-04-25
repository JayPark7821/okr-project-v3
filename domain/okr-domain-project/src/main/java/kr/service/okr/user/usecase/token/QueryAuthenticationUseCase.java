package kr.service.okr.user.usecase.token;

import java.util.Optional;

public interface QueryAuthenticationUseCase {
	Optional<String> queryEmailBy(String token);

}
