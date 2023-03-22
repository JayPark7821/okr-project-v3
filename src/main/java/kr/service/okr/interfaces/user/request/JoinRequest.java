package kr.service.okr.interfaces.user.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record JoinRequest(

	@NotNull(message = "임시 유저ID는 필수 값입니다.")
	String guestUserId,

	@NotNull(message = "사용자 이름은 필수 값입니다.")
	@Size(min = 1, max = 100)
	String name,

	@NotNull(message = "email은 필수 값입니다.")
	@Size(max = 512)
	@Email
	String email,

	@NotNull
	String jobField) {
}
