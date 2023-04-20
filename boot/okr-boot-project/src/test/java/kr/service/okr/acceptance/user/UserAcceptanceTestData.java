package kr.service.okr.acceptance.user;

import kr.service.oauth.platform.SocialPlatform;
import kr.service.okr.user.enums.ProviderType;
import kr.service.okr.user.user.domain.JobField;
import kr.service.okr.user.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserAcceptanceTestData {

	사용자1("appleId", "appleUser", "apple@apple.com", "appleProfileImage", SocialPlatform.APPLE,
		JobField.WEB_SERVER_DEVELOPER),
	사용자2("fakeAppleId", "fakeAppleName", "fakeAppleEmail", "fakeApplePic", SocialPlatform.APPLE,
		JobField.WEB_SERVER_DEVELOPER),
	사용자3("fakeGoogleId", "fakeGoogleName", "fakeGoogleIdEmail", "fakeGoogleIdPic", SocialPlatform.GOOGLE,
		JobField.WEB_SERVER_DEVELOPER);

	private String userId;
	private String username;
	private String email;
	private String profileImage;
	private SocialPlatform socialPlatform;
	private JobField jobField;

	public static User getUser(UserAcceptanceTestData data) {
		return User.builder()
			.userId(data.getUserId())
			.email(data.getEmail())
			.username(data.getUsername())
			.profileImage(data.getProfileImage())
			.providerType(ProviderType.valueOf(data.getSocialPlatform().name()))
			.jobField(data.getJobField())
			.build();
	}
}
