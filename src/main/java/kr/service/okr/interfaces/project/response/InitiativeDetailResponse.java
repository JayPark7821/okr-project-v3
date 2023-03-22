package kr.service.okr.interfaces.project.response;

import kr.service.okr.domain.project.info.TeamMemberUserInfo;

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
