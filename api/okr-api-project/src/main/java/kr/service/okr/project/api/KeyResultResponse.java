package kr.service.okr.project.api;

public record KeyResultResponse(
	String keyResultName,
	String keyResultToken,
	Integer keyResultIndex
) {
}
