package kr.service.okr.domain.project.command;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import kr.service.okr.domain.project.Project;

public record ProjectSaveCommand(

	String objective,

	String sdt,

	String edt,

	List<String> teamMembers) {

	public Project toEntity() {
		LocalDate startDt = LocalDate.parse(this.sdt(), DateTimeFormatter.ISO_DATE);
		LocalDate endDt = LocalDate.parse(this.edt(), DateTimeFormatter.ISO_DATE);

		return Project.builder()
			.startDate(startDt)
			.endDate(endDt)
			.objective(this.objective())
			.progress(0)
			.build();
	}
}
