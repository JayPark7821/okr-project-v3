package kr.jay.okrver3.domain.project.service.command;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import kr.jay.okrver3.domain.project.Project;

public record ProjectSaveCommand(

	String objective,

	String sdt,

	String edt,

	List<String> keyResults,

	List<String> teamMembers) {

	public Project toEntity() {
		LocalDate startDt = LocalDate.parse(this.sdt(), DateTimeFormatter.ISO_DATE);
		LocalDate endDt = LocalDate.parse(this.sdt(), DateTimeFormatter.ISO_DATE);

		return Project.builder()
			.startDate(startDt)
			.endDate(endDt)
			.objective(this.objective())
			.progress(0)
			.keyResultList(this.keyResults())
			.build();
	}
}
