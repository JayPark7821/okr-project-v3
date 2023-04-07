package kr.service.okr.project.domain;

import java.time.LocalDate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.project.usecase.RegisterProjectUseCase;

class ProjectTest {
	private RegisterProjectUseCase sut;

	@Test
	void project_constructor_test() throws Exception {
		final String tooLongObjective = IntStream.range(0, 51)
			.mapToObj(i -> "a")
			.collect(Collectors.joining());

		Assertions.assertThatThrownBy(() -> new Project(null, generateDate(0), generateDate(10)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorCode.OBJECTIVE_IS_REQUIRED.getMessage());

		Assertions.assertThatThrownBy(() -> new Project(tooLongObjective, generateDate(0), generateDate(10)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorCode.OBJECTIVE_IS_TOO_LONG.getMessage());

		Assertions.assertThatThrownBy(() -> new Project("objective", null, generateDate(10)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorCode.PROJECT_START_DATE_IS_REQUIRED.getMessage());

		Assertions.assertThatThrownBy(() -> new Project("objective", generateDate(0), null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorCode.PROJECT_END_DATE_IS_REQUIRED.getMessage());

		Assertions.assertThatThrownBy(() -> new Project("objective", generateDate(10), generateDate(0)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorCode.PROJECT_START_DATE_IS_AFTER_END_DATE.getMessage());

		Assertions.assertThatThrownBy(() -> new Project("objective", generateDate(-10), generateDate(-1)))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(ErrorCode.PROJECT_END_DATE_IS_BEFORE_TODAY.getMessage());

		Assertions.assertThat(new Project("objective", generateDate(0), generateDate(10)).getProjectToken())
			.containsPattern(Pattern.compile("project-[a-zA-Z0-9]{12}"));
	}

	private static LocalDate generateDate(int days) {
		return days >= 0 ? LocalDate.now().plusDays(days) : LocalDate.now().minusDays(Math.abs(days));
	}
}