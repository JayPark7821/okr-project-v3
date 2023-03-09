package kr.jay.okrver3.interfaces.project.response;

import kr.jay.okrver3.domain.project.info.TeamMemberUserInfo;

public record InitiativeDetailResponse(
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
