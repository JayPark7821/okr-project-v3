package kr.service.okr.application.project;

public record UpdateKeyResultCommand(
	String keyResultToken,
	String keyResultName
) {
}
