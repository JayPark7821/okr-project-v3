package kr.service.okr.interfaces.project;

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

import kr.service.okr.application.project.ProjectFacade;
import kr.service.okr.common.Response;
import kr.service.okr.common.exception.ErrorCode;
import kr.service.okr.common.exception.OkrApplicationException;
import kr.service.okr.domain.project.info.InitiativeDetailInfo;
import kr.service.okr.domain.project.info.InitiativeForCalendarInfo;
import kr.service.okr.interfaces.AbstractController;
import kr.service.okr.interfaces.project.request.ProjectInitiativeSaveRequest;
import kr.service.okr.interfaces.project.response.InitiativeDetailResponse;
import kr.service.okr.interfaces.project.response.InitiativeForCalendarResponse;
import kr.service.okr.interfaces.project.response.ProjectInitiativeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/initiative")
public class InitiativeApiController extends AbstractController {

	private final ProjectFacade projectFacade;
	private final ProjectDtoMapper mapper;

	@PostMapping
	ResponseEntity<String> registerInitiative(
		@RequestBody @Valid ProjectInitiativeSaveRequest requestDto,
		Authentication authentication
	) {
		if (LocalDate.now().isAfter(LocalDate.parse(requestDto.edt(), DateTimeFormatter.ISO_DATE)))
			throw new OkrApplicationException(ErrorCode.INITIATIVE_END_DATE_SHOULD_AFTER_TODAY);

		return Response.successCreated(
			projectFacade.registerInitiative(
				mapper.of(requestDto),
				getUserSeqFromAuthentication(authentication)
			)
		);
	}

	@PutMapping("/{initiativeToken}/done")
	ResponseEntity<String> initiativeFinished(
		@PathVariable("initiativeToken") String initiativeToken,
		Authentication authentication
	) {

		return Response.successOk(
			projectFacade.initiativeFinished(
				initiativeToken,
				getUserSeqFromAuthentication(authentication)
			)
		);
	}

	@GetMapping("/list/{keyResultToken}")
	ResponseEntity<Page<ProjectInitiativeResponse>> getInitiativeByKeyResultToken(
		@PathVariable("keyResultToken") String keyResultToken,
		Authentication authentication,
		Pageable pageable
	) {

		return Response.successOk(
			projectFacade.getInitiativeByKeyResultToken(
				keyResultToken,
				getUserSeqFromAuthentication(authentication),
				pageable
			).map(mapper::of)
		);

	}

	@GetMapping("/{initiativeToken}")
	ResponseEntity<InitiativeDetailResponse> getInitiativeBy(
		@PathVariable("initiativeToken") String initiativeToken,
		Authentication authentication
	) {
		InitiativeDetailInfo info = projectFacade.getInitiativeBy(
			initiativeToken,
			getUserSeqFromAuthentication(authentication)
		);

		return Response.successOk(
			mapper.of(info)
		);
	}

	@GetMapping("/date/{date}")
	public ResponseEntity<List<InitiativeForCalendarResponse>> getInitiativeByDate(
		@PathVariable("date") String date,
		Authentication authentication) {

		List<InitiativeForCalendarInfo> info = projectFacade.getInitiativeByDate(
			validateDate(date),
			getUserSeqFromAuthentication(authentication)
		);

		return Response.successOk(
			info.stream().map(mapper::of).toList()
		);
	}

	@GetMapping("/yearmonth/{yearmonth}")
	public ResponseEntity<List<String>> getInitiativeDatesBy(
		@PathVariable("yearmonth") String yearmonth,
		Authentication authentication
	) {
		return Response.successOk(
			projectFacade.getInitiativeDatesBy(
				validateYearMonth(yearmonth),
				getUserSeqFromAuthentication(authentication)
			)
		);
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