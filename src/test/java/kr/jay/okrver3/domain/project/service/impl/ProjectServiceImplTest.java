package kr.jay.okrver3.domain.project.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import kr.jay.okrver3.domain.project.service.ProjectInfo;
import kr.jay.okrver3.domain.user.ProviderType;
import kr.jay.okrver3.domain.user.RoleType;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.interfaces.project.ProjectMasterSaveDto;

@DataJpaTest
@Import(ProjectServiceImpl.class)
class ProjectServiceImplTest {

	@Autowired
	private ProjectServiceImpl sut;

	@Test
	@DisplayName("프로젝트를 생성하면 기대하는 응답(projectToken)을 반환한다.")
	void create_project() throws Exception {

		String projectSdt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String projectEdt = LocalDateTime.now().plusDays(10).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		ProjectInfo projectInfo = sut.registerProject(
			new ProjectMasterSaveDto("projectName", projectSdt, projectEdt, "projectObjective",
				List.of("keyResult1", "keyResult2")));

		assertThat(projectInfo.projectToken()).containsPattern(
			Pattern.compile("project-[a-zA-Z0-9]{12}"));
	}

	@Test
	@DisplayName("projectToken으로 조회하면 기대하는 응답(ProjectResponse)을 반환한다.")
	void retrieve_project_with_project_token() throws Exception {

		User user = new User(1L, "appleId", "appleUser", "apple@apple.com", "appleProfileImage", ProviderType.APPLE,
			RoleType.ADMIN, "pass");

		ProjectInfo projectInfo = sut.getProjectInfoBy("project-123456789012", user);

		assertThat(projectInfo.projectToken()).isEqualTo("projectName");
		assertThat(projectInfo.name()).isEqualTo("projectName");
		assertThat(projectInfo.objective()).isEqualTo("projectObjective");
		assertThat(projectInfo.startDate()).isEqualTo("2020-12-01");
		assertThat(projectInfo.endDate()).isEqualTo("2020-12-12");
		assertThat(projectInfo.projectType()).isEqualTo("SINGLE");
	}

}