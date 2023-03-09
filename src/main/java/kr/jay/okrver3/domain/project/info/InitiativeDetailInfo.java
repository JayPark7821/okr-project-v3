package kr.jay.okrver3.domain.project.info;

public record InitiativeDetailInfo(
	boolean done,
	TeamMemberUserInfo user,
	String startDate,
	String endDate,
	String initiativeToken,
	String initiativeName,
	String initiativeDetail,
	boolean myInitiative
) {
}
