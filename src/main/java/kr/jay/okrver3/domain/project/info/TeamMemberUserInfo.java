package kr.jay.okrver3.domain.project.info;

import kr.jay.okrver3.domain.user.User;

public record TeamMemberUserInfo(
	String userEmail,
	String userName,
	String profileImage,
	String jobField) {

	public TeamMemberUserInfo(User user) {
		this(user.getEmail(), user.getUsername(), user.getProfileImage(), user.getJobField().getTitle());
	}
}
