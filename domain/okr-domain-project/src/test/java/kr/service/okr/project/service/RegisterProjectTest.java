package kr.service.okr.project.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import kr.service.okr.project.repository.ProjectCommand;
import kr.service.okr.project.repository.TestProjectRepository;
import kr.service.okr.project.usecase.RegisterProjectUseCase;

class RegisterProjectTest {

	private RegisterProjectUseCase sut;
	private ProjectCommand projectCommand;

	@BeforeEach
	void setUp() {
		TestProjectRepository.clear();
		this.projectCommand = new TestProjectRepository.TestProjectCommand();
		sut = new RegisterProject(projectCommand);
	}

	@Test
	@Sql("classpath:insert-user.sql")
	@DisplayName("팀원없이 프로젝트를 생성하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project() throws Exception {

		LocalDate projectSdt = LocalDate.now();
		LocalDate projectEdt = LocalDate.now().plusDays(10);

		final String result = sut.registerProject(
			new RegisterProjectUseCase.Command("object", projectSdt, projectEdt, 1L, List.of()));

		System.out.println("result = " + result);
		assertThat(result).containsPattern(Pattern.compile("project-[a-zA-Z0-9]{16}"));

	}
}