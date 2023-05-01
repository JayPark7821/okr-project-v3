package kr.service.okr.api.project;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import kr.service.okr.project.api.RegisterInitiativeRequest;
import kr.service.okr.utils.SpringBootTestReady;
import kr.service.okr.utils.TestHelpUtils;

public class InitiativeApiControllerImplTest extends SpringBootTestReady {

	@Autowired
	private InitiativeApiControllerImpl sut;

	@BeforeEach
	void beforeEach() {
		super.setUp();
		dataLoader.loadData(List.of("/project-test-data.sql"));
	}

	@Test
	void 행동전략_추가시_기대하는_응답을_리턴한다_initiativeToken() throws Exception {

		RegisterInitiativeRequest requestDto = new RegisterInitiativeRequest(
			"key_wV6MX15WQ3DTzQMs",
			"행동전략",
			TestHelpUtils.getDateString(10, "yyyy-MM-dd"),
			TestHelpUtils.getDateString(-10, "yyyy-MM-dd"),
			"행동전략 상세내용"
		);

		ResponseEntity<String> response = sut.registerInitiative(requestDto, getAuthenticationInfo(112L));

		assertThat(response.getBody()).containsPattern(
			Pattern.compile("initiative-[a-zA-Z0-9]{9}"));
	}

}
