package kr.jay.okrver3.interfaces.user.request;

import javax.validation.constraints.NotNull;

public record UserInfoUpdateRequest(

	@NotNull(message = "사용자 이름은 필수 값입니다.")
	String userName,
	String profileImage,
	@NotNull(message = "직업 분야는 필수 값입니다.")
	String jobField
) {
}
