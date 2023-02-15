package kr.jay.okrver3.interfaces.project;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project")
public class ProjectApiController {

	@PostMapping
	public ResponseEntity<String> registerProject(
		@RequestBody @Valid ProjectMasterSaveDto requestDto,
		Authentication authentication
	) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}
