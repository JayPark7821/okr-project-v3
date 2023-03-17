package kr.jay.okrver3.interfaces.project;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.jay.okrver3.application.project.ProjectFacade;
import kr.jay.okrver3.common.Response;
import kr.jay.okrver3.domain.project.aggregate.feedback.SearchRange;
import kr.jay.okrver3.domain.project.info.IniFeedbackInfo;
import kr.jay.okrver3.interfaces.project.request.FeedbackSaveRequest;
import kr.jay.okrver3.interfaces.project.response.FeedbackDetailResponse;
import kr.jay.okrver3.interfaces.project.response.IniFeedbackResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feedback")
public class FeedbackApiController extends AbstractProjectController {

	private final ProjectFacade projectFacade;
	private final ProjectDtoMapper mapper;

	@PostMapping
	public ResponseEntity<String> registerFeedback(
		@RequestBody @Valid FeedbackSaveRequest requestDto,
		Authentication authentication) {

		return Response.successCreated(
			projectFacade.registerFeedback(
				mapper.of(requestDto),
				getUserFromAuthentication(authentication)
			)
		);
	}

	@GetMapping("/{initiativeToken}")
	public ResponseEntity<IniFeedbackResponse> getInitiativeFeedbacksBy(
		@PathVariable("initiativeToken") String initiativeToken,
		Authentication authentication
	) {
		IniFeedbackInfo info = projectFacade.getInitiativeFeedbacksBy(
			initiativeToken,
			getUserFromAuthentication(authentication)
		);

		return Response.successOk(
			mapper.of(info)
		);
	}

	@GetMapping("/count")
	public ResponseEntity<Integer> getCountOfInitiativeToGiveFeedback(
		Authentication authentication
	) {
		return Response.successOk(
			projectFacade.getCountOfInitiativeToGiveFeedback(
				getUserFromAuthentication(authentication)
			)
		);
	}

	@GetMapping
	public ResponseEntity<Page<FeedbackDetailResponse>> getRecievedFeedback(
		String searchRange,
		Authentication authentication,
		Pageable pageable
	) {
		SearchRange range = SearchRange.of(searchRange);
		return Response.successOk(
			projectFacade.getRecievedFeedback(
				range,
				getUserFromAuthentication(authentication),
				pageable
			).map(mapper::of)
		);
	}

}