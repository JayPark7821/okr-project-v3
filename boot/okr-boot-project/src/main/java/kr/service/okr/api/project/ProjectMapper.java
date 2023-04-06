package kr.service.okr.api.project;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import kr.service.okr.application.RegisterProjectCommand;

public class ProjectMapper {

	static RegisterProjectCommand toCommand(RegisterProjectRequestDto request, Long userSeq) {
		return new RegisterProjectCommand(
			request.objective(),
			LocalDate.parse(request.startDate(), DateTimeFormatter.ISO_DATE),
			LocalDate.parse(request.endDate(), DateTimeFormatter.ISO_DATE),
			request.teamMembers(),
			userSeq
		);
	}
}
