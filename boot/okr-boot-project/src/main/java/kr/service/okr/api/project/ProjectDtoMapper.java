package kr.service.okr.api.project;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import kr.service.okr.application.project.RegisterProjectCommand;
import kr.service.okr.project.api.KeyResultResponse;
import kr.service.okr.project.api.ProjectInfoResponse;
import kr.service.okr.project.api.RegisterProjectRequest;
import kr.service.okr.project.usecase.KeyResultInfo;
import kr.service.okr.project.usecase.ProjectInfo;

public class ProjectDtoMapper {

	static RegisterProjectCommand toCommand(RegisterProjectRequest request, Long userSeq) {
		return new RegisterProjectCommand(
			request.objective(),
			LocalDate.parse(request.startDate(), DateTimeFormatter.ISO_DATE),
			LocalDate.parse(request.endDate(), DateTimeFormatter.ISO_DATE),
			request.teamMembers(),
			userSeq
		);
	}

	static ProjectInfoResponse of(ProjectInfo projectInfo) {
		return new ProjectInfoResponse(
			projectInfo.projectToken(),
			projectInfo.objective(),
			projectInfo.startDate(),
			projectInfo.endDate(),
			projectInfo.projectType(),
			projectInfo.keyResultInfos().stream().map(ProjectDtoMapper::of).toList(),
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
