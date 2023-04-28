package kr.service.okr.application.project;

import org.springframework.stereotype.Component;

import kr.service.okr.project.usecase.QueryProjectUseCase;
import kr.service.okr.project.usecase.RegisterProjectUseCase;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProjectFacade {

	private final RegisterProjectUseCase registerProjectUseCase;
	private final QueryProjectUseCase queryProjectUseCase;

	public String registerProject(RegisterProjectCommand requestCommand) {
		return registerProjectUseCase.registerProject(toCommand(requestCommand));
	}

	public ProjectInfo getProjectInfoBy(final String projectToken, final Long userSeq) {

		return new ProjectInfo(
			queryProjectUseCase.queryProjectBy(new QueryProjectUseCase.Query(projectToken, userSeq)),
			null
		);
	}

	private RegisterProjectUseCase.Command toCommand(final RegisterProjectCommand requestCommand) {
		return new RegisterProjectUseCase.Command(
			requestCommand.objective(),
			requestCommand.startDate(),
			requestCommand.endDate(),
			requestCommand.userSeq(),
			requestCommand.teamMembers()
		);
	}
}
