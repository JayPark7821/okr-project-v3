package kr.jay.okrver3.domain.user;

import javax.validation.constraints.NotNull;

public record UserInfoUpdateCommand(

	String userName,
	String profileImage,
	String jobField
) {
}
