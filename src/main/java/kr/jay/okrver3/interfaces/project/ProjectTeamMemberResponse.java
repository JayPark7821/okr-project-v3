package kr.jay.okrver3.interfaces.project;

import kr.jay.okrver3.domain.project.service.ProjectTeamMemberInfo;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.domain.user.service.UserInfo;

public record ProjectTeamMemberResponse(
	String userEmail,
	String userName,
	String profileImage,
	String jobField) {

	public ProjectTeamMemberResponse(User user) {
		this(user.getEmail(), user.getUsername(), user.getProfileImage(), user.getJobField().getTitle());
	}

	public ProjectTeamMemberResponse(UserInfo info){
		this(info.email(), info.name(), info.profileImageUrl(), info.jobFieldDetail().getTitle());
	}

}
