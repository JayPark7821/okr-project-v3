package kr.service.okr.api;

public record KeyResultResponse(
	String keyResultName,
	String keyResultToken,
	Integer keyResultIndex
) {
}
