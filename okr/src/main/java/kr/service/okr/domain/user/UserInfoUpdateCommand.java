package kr.service.okr.domain.user;

public record UserInfoUpdateCommand(

	String userName,
	String profileImage,
	String jobField
) {
}
