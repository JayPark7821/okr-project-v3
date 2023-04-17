package kr.service.okr.application.project;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import kr.service.okr.project.usecase.QueryProjectUseCase;
import kr.service.okr.project.usecase.RegisterProjectUseCase;
import kr.service.user.api.internal.UserInfoResponse;
import kr.service.user.api.internal.UserInternalApiController;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProjectFacade {

	private final RegisterProjectUseCase registerProjectUseCase;
	private final UserInternalApiController userInternalApiController;
	private final QueryProjectUseCase queryProjectUseCase;

	public String registerProject(RegisterProjectCommand requestCommand) {

		return registerProjectUseCase.registerProject(
			new RegisterProjectUseCase.Command(
				requestCommand.objective(),
				requestCommand.startDate(),
				requestCommand.endDate(),
				requestCommand.userSeq(),
				List.of()
			)
		);
	}

	public ProjectInfo getProjectInfoBy(final String projectToken, final String authToken) {
		final ResponseEntity<UserInfoResponse> userInfo = userInternalApiController.getUserInfoBy(authToken);
		final Long userSeq = userInfo.getBody().userSeq();

		return new ProjectInfo(
			queryProjectUseCase.queryProjectBy(new QueryProjectUseCase.Query(projectToken, userSeq)),
			userSeq
		);
	}

}
