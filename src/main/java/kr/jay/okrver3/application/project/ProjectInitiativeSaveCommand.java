package kr.jay.okrver3.application.project;

import java.time.LocalDate;

public record ProjectInitiativeSaveCommand(
	String keyResultToken,
	String name,
	LocalDate sdt,
	LocalDate edt,
	String detail) {
}
