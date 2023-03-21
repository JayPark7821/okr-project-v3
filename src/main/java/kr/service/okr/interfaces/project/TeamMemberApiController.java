package kr.service.okr.interfaces.project;

import javax.validation.Valid;

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
import kr.service.okr.interfaces.AbstractController;
import kr.service.okr.interfaces.project.request.TeamMemberInviteRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/team")
public class TeamMemberApiController extends AbstractController {

	private final ProjectFacade projectFacade;
	private final ProjectDtoMapper mapper;

	@PostMapping("/invite")
	ResponseEntity<String> inviteTeamMember(
		@RequestBody @Valid TeamMemberInviteRequest requestDto,
		Authentication authentication
	) {

		return Response.successCreated(
			projectFacade.inviteTeamMember(
				mapper.of(requestDto),
				getUserSeqFromAuthentication(authentication)
			)
		);
	}

	@GetMapping("/invite/{projectToken}/{email}")
	ResponseEntity<String> validateEmailToInvite(
		@PathVariable("projectToken") String projectToken,
		@PathVariable("email") String email,
		Authentication authentication
	) {

		return Response.successOk(
			projectFacade.validateEmailToInvite(
				projectToken,
				email,
				getUserSeqFromAuthentication(authentication)
			)
		);
	}

}