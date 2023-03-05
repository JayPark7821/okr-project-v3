package kr.jay.okrver3.interfaces.project;

import kr.jay.okrver3.domain.project.service.ProjectInitiativeInfo;

public record ProjectInitiativeResponse(String initiativeToken, String initiativeName, boolean done, ProjectTeamMemberResponse user) {

	public ProjectInitiativeResponse(ProjectInitiativeInfo info){
		this(info.initiativeToken(), info.initiativeName(), info.done(), new ProjectTeamMemberResponse(info.user()));
	}
}
