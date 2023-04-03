package kr.service.okr.interfaces.project;

import static kr.service.okr.util.TestHelpUtils.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.service.okr.interfaces.project.request.ProjectKeyResultSaveRequest;
import kr.service.okr.util.SpringBootTestReady;

class KeyResultApiControllerTest extends SpringBootTestReady {

	@Autowired
	private KeyResultApiController sut;

	@PersistenceContext
	EntityManager em;

	@BeforeEach
	void beforeEach() {
		super.setUp();
		dataLoader.loadData(List.of("/insert-project-data.sql"));
	}

	@Test
	void 프로젝트_핵심결과_추가시_기대하는_응답을_리턴한다_keyResultToken() throws Exception {
		String projectToken = "mst_as3fg34tgg6421";
		String keyResultName = "keyResult";

		UsernamePasswordAuthenticationToken auth = getAuthenticationToken(em, 2L);

		ResponseEntity<String> response = sut.registerKeyResult(
			new ProjectKeyResultSaveRequest(projectToken, keyResultName), auth);

		assertThat(response.getBody()).containsPattern(
			Pattern.compile("keyResult-[a-zA-Z0-9]{10}"));

	}

}