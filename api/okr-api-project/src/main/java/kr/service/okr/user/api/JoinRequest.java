package kr.service.okr.user.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record JoinRequest(
	@NotNull(message = "임시 유저ID는 필수 값입니다.")
	String guestUuid,

	@NotNull(message = "사용자 이름은 필수 값입니다.")
	@Size(min = 1, max = 100)
	String username,

	@NotNull(message = "email은 필수 값입니다.")
	@Size(max = 512)
	@Email
	String email,

	@NotNull
	String jobField) {
}
