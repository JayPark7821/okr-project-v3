package kr.service.okr.interfaces.project;

import java.util.List;

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

import kr.service.okr.application.project.ProjectFacade;
import kr.service.okr.common.Response;
import kr.service.okr.domain.project.aggregate.feedback.SearchRange;
import kr.service.okr.domain.project.info.IniFeedbackInfo;
import kr.service.okr.interfaces.AbstractController;
import kr.service.okr.interfaces.project.request.FeedbackSaveRequest;
import kr.service.okr.interfaces.project.response.FeedbackDetailResponse;
import kr.service.okr.interfaces.project.response.IniFeedbackResponse;
import kr.service.okr.interfaces.project.response.ProjectInitiativeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feedback")
public class FeedbackApiController extends AbstractController {

	private final ProjectFacade projectFacade;
	private final ProjectDtoMapper mapper;

	@PostMapping
	public ResponseEntity<String> registerFeedback(
		@RequestBody @Valid FeedbackSaveRequest requestDto,
		Authentication authentication) {

		return Response.successCreated(
			projectFacade.registerFeedback(
				mapper.of(requestDto),
				getUserSeqFromAuthentication(authentication)
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
			getUserSeqFromAuthentication(authentication)
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
				getUserSeqFromAuthentication(authentication)
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
				getUserSeqFromAuthentication(authentication),
				pageable
			).map(mapper::of)
		);
	}

	@GetMapping("/required")
	public ResponseEntity<List<ProjectInitiativeResponse>> getRequiredFeedbackInitiative(
		Authentication authentication
	) {
		return Response.successOk(
			projectFacade.getRequiredFeedbackInitiative(
				getUserSeqFromAuthentication(authentication)
			).stream().map(mapper::of).toList()
		);
	}
}