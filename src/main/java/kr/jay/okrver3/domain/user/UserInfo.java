package kr.jay.okrver3.domain.user;

public record UserInfo(String id, String name, String email, String profileImageUrl, ProviderType providerType){

	public UserInfo(User user) {
		this(user.getUserId(), user.getUsername(), user.getEmail(), user.getProfileImage(), user.getProviderType());
	}
}
