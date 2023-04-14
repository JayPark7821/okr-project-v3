package kr.service.user.api.internal;

public record UserInfoResponse(
	Long userSeq,
	String email,
	String userId,
	String username
) {
}
