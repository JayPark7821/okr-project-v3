package kr.service.okr.domain.project.info;

import java.time.format.DateTimeFormatter;

import kr.service.okr.domain.project.aggregate.initiative.Initiative;

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

	public InitiativeDetailInfo(Initiative initiative, Long userSeq) {
		this(
			initiative.isDone(),
			new TeamMemberUserInfo(initiative.getTeamMember().getUser()),
			initiative.getSdt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
			initiative.getEdt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
			initiative.getInitiativeToken(),
			initiative.getName(),
			initiative.getDetail(),
			initiative.getTeamMember().getUser().getUserSeq().equals(userSeq)
		);
	}
}
