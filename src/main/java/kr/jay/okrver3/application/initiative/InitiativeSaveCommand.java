package kr.jay.okrver3.application.initiative;

import java.time.LocalDate;

public record InitiativeSaveCommand(
	String keyResultToken,
	String name,
	LocalDate sdt,
	LocalDate edt,
	String detail) {
}
