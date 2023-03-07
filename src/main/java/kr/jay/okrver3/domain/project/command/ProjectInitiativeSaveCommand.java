package kr.jay.okrver3.domain.project.command;

import java.time.LocalDate;

public record ProjectInitiativeSaveCommand(
	String keyResultToken,
	String name,
	LocalDate sdt,
	LocalDate edt,
	String detail) {
}
