package kr.jay.okrver3.application.project;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.RoleType;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.interfaces.project.ProjectMasterSaveDto;

@DataJpaTest
@Import(ProjectFacade.class)
class ProjectFacadeTest {

	@Autowired
	private ProjectFacade sut;

	@Test
	@Sql("classpath:insert-user.sql")
	@DisplayName("프로젝트를 생성하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project() throws Exception {

		User user = new User(1L, "appleId", "appleUser", "apple@apple.com", "appleProfileImage", ProviderType.APPLE,
			RoleType.ADMIN, "pass");

		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		String projectToken = sut.registerProject(
			new ProjectMasterSaveDto("projectName", projectSdt, projectEdt, "projectObjective",
				List.of("keyResult1", "keyResult2")), user);

		assertThat(projectToken).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));
	}

}