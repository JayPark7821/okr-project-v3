package kr.service.user.api.internal;

public record UserInfo(
	Long userSeq,
	String email,
	String userId,
	String username
) {
}
