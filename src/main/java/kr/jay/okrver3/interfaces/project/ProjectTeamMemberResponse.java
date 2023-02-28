package kr.jay.okrver3.interfaces.project;

import kr.jay.okrver3.domain.user.User;

public record ProjectTeamMemberResponse(
	String userEmail,
	String userName,
	String profileImage,
	String jobField) {

	public ProjectTeamMemberResponse(User user) {
		this(user.getEmail(), user.getUsername(), user.getProfileImage(), user.getJobField().getTitle());
	}
}
