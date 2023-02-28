package kr.jay.okrver3.interfaces.project;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
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
import kr.jay.okrver3.common.utils.ClassUtils;
import kr.jay.okrver3.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProjectApiController {

	private final ProjectFacade projectFacade;

	@PostMapping("/project")
	ResponseEntity<String> registerProject(
		@RequestBody @Valid ProjectMasterSaveDto requestDto,
		Authentication authentication
	) {
		User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_USER_FAILED));

		return Response.success(
			HttpStatus.CREATED,
			projectFacade.registerProject(requestDto, user)
		);
	}

	@GetMapping("/project/{projectToken}")
	ResponseEntity<ProjectInfoResponse> getProjectInfoBy(
		@PathVariable("projectToken") String projectToken,
		Authentication authentication
	) {
		User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_USER_FAILED));

		return Response.success(
			HttpStatus.CREATED,
			new ProjectInfoResponse(
				projectFacade.getProjectInfoBy(projectToken, user))
		);

	}

	@PostMapping("/team/invite")
	ResponseEntity<String> inviteTeamMember(
		@RequestBody @Valid TeamMemberInviteRequestDto teamMemberInviteRequestDto,
		Authentication authentication
	) {

		User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_USER_FAILED));

		return Response.success(
			HttpStatus.CREATED,
			projectFacade.inviteTeamMember(teamMemberInviteRequestDto, user)
		);
	}

	@GetMapping("/team/invite/{projectToken}/{email}")
	ResponseEntity<String> validateEmail(
		@PathVariable("projectToken") String projectToken,
		@PathVariable("email") String email,
		Authentication authentication
	) {

		User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.CASTING_USER_FAILED));

		return Response
			.success(
				HttpStatus.OK,
				projectFacade.validateEmail(projectToken, email, user)
			);
	}
}
