package kr.jay.okrver3.domain.project.info;

import kr.jay.okrver3.domain.project.aggregate.initiative.Initiative;

public record ProjectInitiativeInfo(String initiativeToken, String initiativeName, boolean done,
									ProjectTeamMemberUserInfo user) {
	public ProjectInitiativeInfo(Initiative initiative) {
		this(initiative.getInitiativeToken(), initiative.getName(), initiative.isDone(),
			new ProjectTeamMemberUserInfo(initiative.getTeamMember().getUser()));
	}
}
