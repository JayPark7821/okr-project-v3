package kr.service.okr.project.usecase;

public record RegisterInitiativeInfo(
	String initiativeToken,
	Long projectId
) {
}
