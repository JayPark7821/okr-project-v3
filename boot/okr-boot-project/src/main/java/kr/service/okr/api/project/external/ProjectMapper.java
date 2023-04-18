package kr.service.okr.api.project.external;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import kr.service.okr.application.project.KeyResultInfo;
import kr.service.okr.application.project.ProjectInfo;
import kr.service.okr.application.project.RegisterProjectCommand;
import kr.service.okr.project.api.KeyResultResponse;
import kr.service.okr.project.api.ProjectInfoResponse;
import kr.service.okr.project.api.RegisterProjectRequestDto;

public class ProjectMapper {

	static RegisterProjectCommand toCommand(RegisterProjectRequestDto request, String authToken) {
		return new RegisterProjectCommand(
			request.objective(),
			LocalDate.parse(request.startDate(), DateTimeFormatter.ISO_DATE),
			LocalDate.parse(request.endDate(), DateTimeFormatter.ISO_DATE),
			request.teamMembers(),
			authToken
		);
	}

	static ProjectInfoResponse of(ProjectInfo projectInfo) {
		return new ProjectInfoResponse(
			projectInfo.projectToken(),
			projectInfo.objective(),
			projectInfo.startDate(),
			projectInfo.endDate(),
			projectInfo.projectType(),
			projectInfo.keyResultInfos().stream().map(ProjectMapper::of).toList(),
			projectInfo.teamMembersCount(),
			projectInfo.roleType()
		);
	}

	static KeyResultResponse of(KeyResultInfo keyResultInfo) {
		return new KeyResultResponse(
			keyResultInfo.name(),
			keyResultInfo.keyResultToken(),
			keyResultInfo.keyResultIndex()
		);
	}
}
