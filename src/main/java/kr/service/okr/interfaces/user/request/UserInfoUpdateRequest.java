package kr.service.okr.interfaces.user.request;

public record UserInfoUpdateRequest(

	String userName,
	String profileImage,
	String jobField
) {
}
