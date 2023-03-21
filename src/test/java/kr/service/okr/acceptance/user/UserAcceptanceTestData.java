package kr.service.okr.acceptance.user;

import kr.service.okr.domain.user.JobField;
import kr.service.okr.domain.user.ProviderType;
import kr.service.okr.domain.user.RoleType;
import kr.service.okr.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserAcceptanceTestData {

	사용자1("appleId", "appleUser", "apple@apple.com", "appleProfileImage", ProviderType.APPLE, RoleType.USER,
		JobField.WEB_SERVER_DEVELOPER),
	사용자2("fakeAppleId", "fakeAppleName", "fakeAppleEmail", "fakeApplePic", ProviderType.APPLE, RoleType.USER,
		JobField.WEB_SERVER_DEVELOPER),
	사용자3("fakeGoogleId", "fakeGoogleName", "fakeGoogleIdEmail", "fakeGoogleIdPic", ProviderType.GOOGLE, RoleType.USER,
		JobField.WEB_SERVER_DEVELOPER);

	private String userId;
	private String username;
	private String email;
	private String profileImage;
	private ProviderType providerType;
	private RoleType roleType;
	private JobField jobField;

	public static User getUser(UserAcceptanceTestData data) {
		return User.builder()
			.userId(data.getUserId())
			.email(data.getEmail())
			.username(data.getUsername())
			.roleType(data.getRoleType())
			.profileImage(data.getProfileImage())
			.providerType(data.getProviderType())
			.jobField(data.getJobField())
			.build();
	}
}
