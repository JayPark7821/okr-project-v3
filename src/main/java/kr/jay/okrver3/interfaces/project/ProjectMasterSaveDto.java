package kr.jay.okrver3.interfaces.project;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.project.ProjectType;

public record ProjectMasterSaveDto(String name, String sdt, String edt, String objective, List<String> keyResults) {

	public Project toEntity() {
		LocalDate startDt = LocalDate.parse(sdt, DateTimeFormatter.ofPattern("yyyyMMdd"));
		LocalDate endDt = LocalDate.parse(edt, DateTimeFormatter.ofPattern("yyyyMMdd"));

		return Project.builder()
			.name(name)
			.startDate(startDt)
			.endDate(endDt)
			.type(ProjectType.SINGLE)
			.objective(objective)
			.progress(0)
			.build();
	}

}
