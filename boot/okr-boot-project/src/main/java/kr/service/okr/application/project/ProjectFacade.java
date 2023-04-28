package kr.service.okr.application.project;

import org.springframework.stereotype.Component;

import kr.service.okr.project.usecase.ProjectInfo;
import kr.service.okr.project.usecase.QueryProjectInfoUseCase;
import kr.service.okr.project.usecase.RegisterProjectUseCase;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProjectFacade {

	private final RegisterProjectUseCase registerProjectUseCase;
	private final QueryProjectInfoUseCase queryProjectInfoUseCase;

	public String registerProject(RegisterProjectCommand requestCommand) {
		return registerProjectUseCase.command(toCommand(requestCommand));
	}

	public ProjectInfo getProjectInfoBy(final String projectToken, final Long userSeq) {
		return queryProjectInfoUseCase.query(new QueryProjectInfoUseCase.Query(projectToken, userSeq));
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
