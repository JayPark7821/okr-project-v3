package kr.service.okr.user.domain;

import kr.service.okr.project.exception.ErrorCode;
import kr.service.okr.project.exception.OkrProjectDomainException;
import lombok.Builder;
import lombok.Getter;

@Getter
public class User {
	private Long userSeq;
	private String userId;
	private String username;
	private String email;

	@Builder
	private User(final Long userSeq, final String userId, final String username, final String email) {
		if (userSeq == null || userId == null || username == null || email == null)
			throw new OkrProjectDomainException(ErrorCode.INTERNAL_SERVER_ERROR);

		this.userSeq = userSeq;
		this.userId = userId;
		this.username = username;
		this.email = email;
	}

	public User(final String userId, final String username, final String email) {
		this.userId = userId;
		this.username = username;
		this.email = email;
	}
}
