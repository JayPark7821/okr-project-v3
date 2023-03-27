package kr.service.okr.interfaces.project.response;

public record ProjectInitiativeResponse(String initiativeToken, String initiativeName, boolean done,
										ProjectTeamMemberResponse user) {

}
