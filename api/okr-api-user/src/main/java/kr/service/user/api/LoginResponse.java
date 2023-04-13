package kr.service.user.api;

public record LoginResponse(
	String guestUserId,
	String email,
	String name,
	String providerType,
	String profileImage,
	String accessToken,
	String refreshToken,
	String jobFieldDetail
) {

}
