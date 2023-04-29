package kr.service.okr.api.project;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import kr.service.okr.project.api.RegisterKeyResultRequest;
import kr.service.okr.utils.SpringBootTestReady;

public class KeyResultApiControllerImplTest extends SpringBootTestReady {

	@Autowired
	private KeyResultApiControllerImpl sut;

	@BeforeEach
	void beforeEach() {
		super.setUp();
		dataLoader.loadData(List.of("/project-test-data.sql"));
	}

	@Test
	void 프로젝트_핵심결과_추가시_기대하는_응답을_리턴한다_keyResultToken() throws Exception {
		String projectToken = "mst_qfeeffea223fef";
		String keyResultName = "keyResult";

		ResponseEntity<String> response = sut.registerKeyResult(
			new RegisterKeyResultRequest(projectToken, keyResultName), getAuthenticationInfo(112L));

		assertThat(response.getBody()).containsPattern(
			Pattern.compile("keyResult-[a-zA-Z0-9]{10}"));

	}
}
