package kr.jay.okrver3.interfaces.project;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.jay.okrver3.application.project.ProjectFacade;
import kr.jay.okrver3.common.Response;
import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.common.utils.ClassUtils;
import kr.jay.okrver3.domain.project.ProjectType;
import kr.jay.okrver3.domain.project.SortType;
import kr.jay.okrver3.domain.project.aggregate.feedback.SearchRange;
import kr.jay.okrver3.domain.project.command.ProjectDetailRetrieveCommand;
import kr.jay.okrver3.domain.project.info.IniFeedbackInfo;
import kr.jay.okrver3.domain.project.info.InitiativeDetailInfo;
import kr.jay.okrver3.domain.project.info.InitiativeForCalendarInfo;
import kr.jay.okrver3.domain.project.info.ProjectSideMenuInfo;
import kr.jay.okrver3.domain.user.User;
import kr.jay.okrver3.interfaces.project.request.FeedbackSaveRequest;
import kr.jay.okrver3.interfaces.project.request.ProjectInitiativeSaveRequest;
import kr.jay.okrver3.interfaces.project.request.ProjectKeyResultSaveRequest;
import kr.jay.okrver3.interfaces.project.request.ProjectSaveRequest;
import kr.jay.okrver3.interfaces.project.request.TeamMemberInviteRequest;
import kr.jay.okrver3.interfaces.project.response.FeedbackDetailResponse;
import kr.jay.okrver3.interfaces.project.response.IniFeedbackResponse;
import kr.jay.okrver3.interfaces.project.response.InitiativeDetailResponse;
import kr.jay.okrver3.interfaces.project.response.InitiativeForCalendarResponse;
import kr.jay.okrver3.interfaces.project.response.ProjectDetailResponse;
import kr.jay.okrver3.interfaces.project.response.ProjectInfoResponse;
import kr.jay.okrver3.interfaces.project.response.ProjectInitiativeResponse;
import kr.jay.okrver3.interfaces.project.response.ProjectSideMenuResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProjectApiController {

	private final ProjectFacade projectFacade;
	private final ProjectDtoMapper mapper;

	@PostMapping("/project")
	ResponseEntity<String> registerProject(
		@RequestBody @Valid ProjectSaveRequest requestDto,
		Authentication authentication
	) {

		return Response.successCreated(
			projectFacade.registerProject(mapper.of(requestDto), getUserFromAuthentication(authentication))
		);
	}

	@GetMapping("/project/{projectToken}")
	ResponseEntity<ProjectInfoResponse> getProjectInfoBy(
		@PathVariable("projectToken") String projectToken,
		Authentication authentication
	) {

		return Response.successCreated(
			mapper.of(projectFacade.getProjectInfoBy(projectToken, getUserFromAuthentication(authentication)))
		);
	}

	@GetMapping("/project")
	ResponseEntity<Page<ProjectDetailResponse>> getDetailProjectList(
		String sortType,
		String includeFinishedProjectYN,
		String projectType,
		Authentication authentication,
		Pageable pageable
	) {

		ProjectDetailRetrieveCommand command = mapper.of(SortType.of(sortType),
			ProjectType.of(projectType),
			validateIncludeFinishedProjectYN(includeFinishedProjectYN),
			pageable
		);

		return Response.successOk(
			projectFacade.getDetailProjectList(
				command,
				getUserFromAuthentication(authentication)
			).map(mapper::of)
		);

	}

	@PostMapping("/team/invite")
	ResponseEntity<String> inviteTeamMember(
		@RequestBody @Valid TeamMemberInviteRequest requestDto,
		Authentication authentication
	) {

		return Response.successCreated(
			projectFacade.inviteTeamMember(
				mapper.of(requestDto),
				getUserFromAuthentication(authentication)
			)
		);
	}

	@GetMapping("/team/invite/{projectToken}/{email}")
	ResponseEntity<String> validateEmailToInvite(
		@PathVariable("projectToken") String projectToken,
		@PathVariable("email") String email,
		Authentication authentication
	) {

		return Response.successOk(
			projectFacade.validateEmailToInvite(
				projectToken,
				email,
				getUserFromAuthentication(authentication)
			)
		);
	}

	@GetMapping("/project/{projectToken}/side")
	ResponseEntity<ProjectSideMenuResponse> getProjectSideMenuDetails(
		@PathVariable("projectToken") String projectToken,
		Authentication authentication
	) {

		ProjectSideMenuInfo info = projectFacade.getProjectSideMenuDetails(
			projectToken,
			getUserFromAuthentication(authentication)
		);

		return Response.successOk(
			mapper.of(info)
		);
	}

	@PostMapping("/keyresult")
	ResponseEntity<String> registerKeyResult(
		@RequestBody @Valid ProjectKeyResultSaveRequest requestDto,
		Authentication authentication
	) {

		return Response.successCreated(
			projectFacade.registerKeyResult(
				mapper.of(requestDto),
				getUserFromAuthentication(authentication)
			)
		);
	}

	@PostMapping("/initiative")
	ResponseEntity<String> registerInitiative(
		@RequestBody @Valid ProjectInitiativeSaveRequest requestDto,
		Authentication authentication
	) {
		if (LocalDate.now().isAfter(LocalDate.parse(requestDto.edt(), DateTimeFormatter.ISO_DATE)))
			throw new OkrApplicationException(ErrorCode.INITIATIVE_END_DATE_SHOULD_AFTER_TODAY);

		return Response.successCreated(
			projectFacade.registerInitiative(
				mapper.of(requestDto),
				getUserFromAuthentication(authentication)
			)
		);
	}

	@PutMapping("/initiative/{initiativeToken}/done")
	ResponseEntity<String> initiativeFinished(
		@PathVariable("initiativeToken") String initiativeToken,
		Authentication authentication
	) {

		return Response.successOk(
			projectFacade.initiativeFinished(
				initiativeToken,
				getUserFromAuthentication(authentication)
			)
		);
	}

	@GetMapping("/initiative/list/{keyResultToken}")
	ResponseEntity<Page<ProjectInitiativeResponse>> getInitiativeByKeyResultToken(
		@PathVariable("keyResultToken") String keyResultToken,
		Authentication authentication,
		Pageable pageable
	) {

		return Response.successOk(
			projectFacade.getInitiativeByKeyResultToken(
				keyResultToken,
				getUserFromAuthentication(authentication),
				pageable
			).map(mapper::of)
		);

	}

	@PostMapping("/feedback")
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

	@GetMapping("/initiative/{initiativeToken}")
	ResponseEntity<InitiativeDetailResponse> getInitiativeBy(
		@PathVariable("initiativeToken") String initiativeToken,
		Authentication authentication
	) {
		InitiativeDetailInfo info = projectFacade.getInitiativeBy(
			initiativeToken,
			getUserFromAuthentication(authentication)
		);

		return Response.successOk(
			mapper.of(info)
		);
	}

	@GetMapping("/initiative/date/{date}")
	public ResponseEntity<List<InitiativeForCalendarResponse>> getInitiativeByDate(
		@PathVariable("date") String date,
		Authentication authentication) {

		List<InitiativeForCalendarInfo> info = projectFacade.getInitiativeByDate(
			validateDate(date),
			getUserFromAuthentication(authentication)
		);

		return Response.successOk(
			info.stream().map(mapper::of).toList()
		);
	}

	@GetMapping("/initiative/yearmonth/{yearmonth}")
	public ResponseEntity<List<String>> getInitiativeDatesBy(
		@PathVariable("yearmonth") String yearmonth,
		Authentication authentication
	) {
		return Response.successOk(
			projectFacade.getInitiativeDatesBy(
				validateYearMonth(yearmonth),
				getUserFromAuthentication(authentication)
			)
		);
	}

	@GetMapping("/feedback/{initiativeToken}")
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

	@GetMapping("/feedback/count")
	public ResponseEntity<Integer> getCountOfInitiativeToGiveFeedback(
		Authentication authentication
	) {
		return Response.successOk(
			projectFacade.getCountOfInitiativeToGiveFeedback(
				getUserFromAuthentication(authentication)
			)
		);
	}

	@GetMapping("/feedback")
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

	//------------------ initiative 관련 api ------------------//
	// TODO :: initiative update

	//------------------ feedback 관련 api ------------------//
	// TODO :: 피드백 확인 처리 api

	//------------------ notification 관련 api ------------------//
	// TODO :: notification 읽음 처리
	// TODO :: notification 삭제 처리

	//------------------ user 관련 api ------------------//
	// TODO :: 회원가입탈퇴 api
	// TODO :: apple login
	// TODO :: 회원정보 변경 api

	private Long getUserFromAuthentication(Authentication authentication) {
		return ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_FAILED))
			.getUserSeq();
	}

	private String validateIncludeFinishedProjectYN(String includeFinishedProjectYN) {
		String finishedProjectYN = includeFinishedProjectYN == null ? "N" : includeFinishedProjectYN.toUpperCase();
		if (finishedProjectYN.matches("[YN]"))
			return finishedProjectYN;

		throw new OkrApplicationException(ErrorCode.INVALID_FINISHED_PROJECT_YN);
	}

	public static YearMonth validateYearMonth(String yearMonth) {
		try {
			return yearMonth == null ? YearMonth.now() :
				YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyy-MM"));
		} catch (Exception e) {
			throw new OkrApplicationException(ErrorCode.INVALID_YEARMONTH_FORMAT);
		}
	}

	private LocalDate validateDate(String date) {
		try {
			return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
		} catch (Exception e) {
			throw new OkrApplicationException(ErrorCode.INVALID_SEARCH_DATE_FORM);
		}
	}

}