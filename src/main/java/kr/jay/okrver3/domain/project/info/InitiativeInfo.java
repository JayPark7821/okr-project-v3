package kr.jay.okrver3.domain.project.info;

import kr.jay.okrver3.domain.project.aggregate.initiative.Initiative;

public record InitiativeInfo(String initiativeToken, String initiativeName, boolean done,
							 TeamMemberUserInfo user) {
	public InitiativeInfo(Initiative initiative) {
		this(initiative.getInitiativeToken(), initiative.getName(), initiative.isDone(),
			new TeamMemberUserInfo(initiative.getTeamMember().getUser()));
	}
}
