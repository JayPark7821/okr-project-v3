package kr.service.okr.interfaces.project.response;

public record ProjectTeamMemberResponse(
	String userEmail,
	String userName,
	String profileImage,
	String jobField) {
}
