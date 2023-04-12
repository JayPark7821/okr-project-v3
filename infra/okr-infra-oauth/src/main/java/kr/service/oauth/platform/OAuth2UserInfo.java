package kr.service.oauth.platform;

public record OAuth2UserInfo(
	String id,
	String username,
	String email,
	String profileImage,
	String socialPlatform) {

}