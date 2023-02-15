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
import kr.jay.okrver3.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project")
public class ProjectApiController {

	private final ProjectFacade projectFacade;

	@PostMapping
	ResponseEntity<String> registerProject(
		@RequestBody @Valid ProjectMasterSaveDto requestDto,
		Authentication authentication
	) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(projectFacade.registerProject(requestDto, (User)authentication.getPrincipal()));
	}

	@GetMapping("/{projectToken}")
	ResponseEntity<ProjectInfoResponse> getProjectInfoBy(
		@PathVariable("projectToken") String projectToken,
		Authentication authentication
	) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(new ProjectInfoResponse(
				projectFacade.getProjectInfoBy(projectToken, (User)authentication.getPrincipal())));
	}

	@PostMapping("/team/invite")
	ResponseEntity<String> inviteTeamMember(
		@RequestBody @Valid TeamMemberInviteRequestDto teamMemberInviteRequestDto,
		Authentication authentication
	) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(projectFacade.inviteTeamMember(teamMemberInviteRequestDto, (User)authentication.getPrincipal()));
	}
}
