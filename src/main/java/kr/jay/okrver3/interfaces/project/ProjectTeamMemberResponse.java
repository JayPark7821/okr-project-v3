package kr.jay.okrver3.interfaces.project;

public record ProjectTeamMemberResponse(
	String userEmail,
	String userName,
	String profileImage,
	String jobField) {
}
