package kr.service.okr.user.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.user.user.domain.User;
import kr.service.okr.user.user.repository.UserQuery;
import kr.service.okr.user.user.usecase.QueryUserUseCase;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QueryUser implements QueryUserUseCase {

	private final UserQuery userQuery;

	@Override
	public Optional<User> query(final String email) {
		return userQuery.findByEmail(email);
	}

	@Override
	public List<Long> query(final List<String> emails) {
		final List<Long> userSeqs = userQuery.findUserSeqsByEmails(emails);
		if (userSeqs.size() != emails.size()) {
			throw new OkrApplicationException(ErrorCode.INVALID_USER_EMAIL);
		}
		return userSeqs;
	}
}
