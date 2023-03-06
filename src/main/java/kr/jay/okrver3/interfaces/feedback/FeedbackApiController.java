package kr.jay.okrver3.interfaces.feedback;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.jay.okrver3.interfaces.feedback.request.FeedbackSaveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feedback")
public class FeedbackApiController {

	@PostMapping
	public ResponseEntity<String> registerFeedback(
		@RequestBody @Valid FeedbackSaveRequest requestDto,
		Authentication authentication) {
		throw new UnsupportedOperationException(
			"kr.jay.okrver3.interfaces.feedback.FeedbackApiController.registerFeedback())");
	}
}
