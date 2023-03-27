package kr.service.okr.interfaces.project;

import java.util.List;

import jakarta.validation.Valid;

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
import kr.service.okr.common.exception.ErrorCode;
import kr.service.okr.common.exception.OkrApplicationException;
import kr.service.okr.domain.project.ProjectType;
import kr.service.okr.domain.project.SortType;
import kr.service.okr.domain.project.command.ProjectDetailRetrieveCommand;
import kr.service.okr.domain.project.info.ParticipateProjectInfo;
import kr.service.okr.domain.project.info.ProjectSideMenuInfo;
import kr.service.okr.interfaces.AbstractController;
import kr.service.okr.interfaces.project.request.ProjectSaveRequest;
import kr.service.okr.interfaces.project.response.ParticipateProjectResponse;
import kr.service.okr.interfaces.project.response.ProjectDetailResponse;
import kr.service.okr.interfaces.project.response.ProjectInfoResponse;
import kr.service.okr.interfaces.project.response.ProjectSideMenuResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project")
public class ProjectApiController extends AbstractController {

	private final ProjectFacade projectFacade;
	private final ProjectDtoMapper mapper;

	@PostMapping
	ResponseEntity<String> registerProject(
		@RequestBody @Valid ProjectSaveRequest requestDto,
		Authentication authentication
	) {

		return Response.successCreated(
			projectFacade.registerProject(mapper.of(requestDto), getUserSeqFromAuthentication(authentication))
		);
	}

	@GetMapping("/{projectToken}")
	ResponseEntity<ProjectInfoResponse> getProjectInfoBy(
		@PathVariable("projectToken") String projectToken,
		Authentication authentication
	) {

		return Response.successOk(
			mapper.of(projectFacade.getProjectInfoBy(projectToken, getUserSeqFromAuthentication(authentication)))
		);
	}

	@GetMapping
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
				getUserSeqFromAuthentication(authentication)
			).map(mapper::of)
		);

	}

	@GetMapping("/{projectToken}/side")
	ResponseEntity<ProjectSideMenuResponse> getProjectSideMenuDetails(
		@PathVariable("projectToken") String projectToken,
		Authentication authentication
	) {

		ProjectSideMenuInfo info = projectFacade.getProjectSideMenuDetails(
			projectToken,
			getUserSeqFromAuthentication(authentication)
		);

		return Response.successOk(
			mapper.of(info)
		);
	}

	@GetMapping("/participate")
	ResponseEntity<List<ParticipateProjectResponse>> getParticipateProjects(
		final Authentication authentication
	) {

		List<ParticipateProjectInfo> info =
			projectFacade.getParticipateProjects(getUserSeqFromAuthentication(authentication));

		return Response.successOk(
			info.stream().map(mapper::of).toList()
		);
	}

	//------------------ initiative 관련 api ------------------//
	// TODO :: initiative update

	//------------------ feedback 관련 api ------------------//
	// TODO :: 피드백 확인 처리 api

	private String validateIncludeFinishedProjectYN(String includeFinishedProjectYN) {
		String finishedProjectYN = includeFinishedProjectYN == null ? "N" : includeFinishedProjectYN.toUpperCase();
		if (finishedProjectYN.matches("[YN]"))
			return finishedProjectYN;

		throw new OkrApplicationException(ErrorCode.INVALID_FINISHED_PROJECT_YN);
	}

}