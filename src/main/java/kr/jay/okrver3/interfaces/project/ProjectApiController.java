package kr.jay.okrver3.interfaces.project;

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

import kr.jay.okrver3.application.project.ProjectFacade;
import kr.jay.okrver3.common.Response;
import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.project.ProjectType;
import kr.jay.okrver3.domain.project.SortType;
import kr.jay.okrver3.domain.project.command.ProjectDetailRetrieveCommand;
import kr.jay.okrver3.domain.project.info.ParticipateProjectInfo;
import kr.jay.okrver3.domain.project.info.ProjectSideMenuInfo;
import kr.jay.okrver3.interfaces.project.request.ProjectSaveRequest;
import kr.jay.okrver3.interfaces.project.response.ProjectDetailResponse;
import kr.jay.okrver3.interfaces.project.response.ProjectInfoResponse;
import kr.jay.okrver3.interfaces.project.response.ProjectSideMenuResponse;
import kr.jay.okrver3.interfaces.project.response.ParticipateProjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project")
public class ProjectApiController extends AbstractProjectController {

	private final ProjectFacade projectFacade;
	private final ProjectDtoMapper mapper;

	@PostMapping
	ResponseEntity<String> registerProject(
		@RequestBody @Valid ProjectSaveRequest requestDto,
		Authentication authentication
	) {

		return Response.successCreated(
			projectFacade.registerProject(mapper.of(requestDto), getUserFromAuthentication(authentication))
		);
	}

	@GetMapping("/{projectToken}")
	ResponseEntity<ProjectInfoResponse> getProjectInfoBy(
		@PathVariable("projectToken") String projectToken,
		Authentication authentication
	) {

		return Response.successCreated(
			mapper.of(projectFacade.getProjectInfoBy(projectToken, getUserFromAuthentication(authentication)))
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
				getUserFromAuthentication(authentication)
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
			getUserFromAuthentication(authentication)
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
			projectFacade.getParticipateProjects(getUserFromAuthentication(authentication));

		return Response.successOk(
			info.stream().map(mapper::of).toList()
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

	private String validateIncludeFinishedProjectYN(String includeFinishedProjectYN) {
		String finishedProjectYN = includeFinishedProjectYN == null ? "N" : includeFinishedProjectYN.toUpperCase();
		if (finishedProjectYN.matches("[YN]"))
			return finishedProjectYN;

		throw new OkrApplicationException(ErrorCode.INVALID_FINISHED_PROJECT_YN);
	}



}