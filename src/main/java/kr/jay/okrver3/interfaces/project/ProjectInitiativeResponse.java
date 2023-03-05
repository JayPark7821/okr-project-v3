package kr.jay.okrver3.interfaces.project;

public record ProjectInitiativeResponse(String initiativeToken, String initiativeName, boolean done, ProjectTeamMemberResponse user) {
}
