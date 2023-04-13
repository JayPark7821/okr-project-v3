package kr.service.okr.application.project;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import kr.service.okr.project.domain.Project;
import kr.service.okr.project.usecase.RegisterProjectUseCase;
import kr.service.user.api.internal.UserInfo;
import kr.service.user.api.internal.UserInternalApiController;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProjectFacade {

	private final RegisterProjectUseCase registerProjectUseCase;
	private final UserInternalApiController userInternalApiController;

	public String registerProject(RegisterProjectCommand requestCommand) {

		final RegisterProjectUseCase.Command command = new RegisterProjectUseCase.Command(
			requestCommand.objective(),
			requestCommand.startDate(),
			requestCommand.endDate(),
			requestCommand.userSeq(),
			List.of()
		);

		return registerProjectUseCase.registerProject(command);
	}

	public Project getProjectInfoBy(final String projectToken, final String authToken) {
		final ResponseEntity<UserInfo> userInfo = userInternalApiController.getUserSeqByEmail(authToken);
		return null;
	}
}
