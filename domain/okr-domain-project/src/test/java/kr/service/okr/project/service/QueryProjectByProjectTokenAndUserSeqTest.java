package kr.service.okr.project.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.service.okr.project.repository.ProjectQuery;
import kr.service.okr.project.repository.TestProjectRepository;
import kr.service.okr.project.usecase.QueryProjectByProjectTokenAndUserSeqUseCase;

class QueryProjectByProjectTokenAndUserSeqTest {

	private QueryProjectByProjectTokenAndUserSeqUseCase sut;
	private ProjectQuery projectQuery;

	@BeforeEach
	void setUp() {
		TestProjectRepository.clear();
		this.projectQuery = new TestProjectRepository.TestProjectQuery();
		sut = new QueryProjectByProjectTokenAndUserSeq(projectQuery);
	}

	@Test
	@DisplayName("projectToken으로 조회하면 기대하는 응답(ProjectResponse)을 반환한다.")
	void retrieve_project_with_project_token() throws Exception {

		// Project.builder()
		// 	.projectToken("project-fgFHxGWeIUQt")
		// 		.
		// TestProjectRepository.persistence.put(1L, );
		//
		// ProjectInfo projectInfo = sut.getProjectInfoBy("project-fgFHxGWeIUQt", 999L);

	}

	// @Test
	// @DisplayName("projectToken으로 조회하면 기대하는 응답(ProjectResponse)을 반환하고 isNew를 변경한다.")
	// void retrieve_project_with_project_token() throws Exception {
	//
	// 	// ProjectInfo projectInfo = sut.getProjectInfoBy("project-fgFHxGWeIUQt", 999L);
	//
	//
	// }
	//
	// @Test
	// @DisplayName("잘못된 projectToken으로 조회하면 기대하는 응답(exception)을 반환한다.")
	// void retrieve_project_with_project_token() throws Exception {
	//
	// 	// ProjectInfo projectInfo = sut.getProjectInfoBy("project-fgFHxGWeIUQt", 999L);
	//
	//
	// }

}