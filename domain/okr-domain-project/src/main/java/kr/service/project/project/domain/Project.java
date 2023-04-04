package kr.service.project.project.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import kr.service.model.project.ProjectType;
import kr.service.project.project.aggregate.keyresult.domain.KeyResult;
import kr.service.project.project.aggregate.team.domain.TeamMember;

public record Project(

	Long id,
	String projectToken,
	List<TeamMember> teamMember,
	List<KeyResult> keyResults,
	LocalDate startDate,
	LocalDate endDate,
	ProjectType type,
	String objective,
	double progress,
	boolean deleted,
	String createdBy,
	String lastModifiedBy,
	LocalDateTime createdDate,
	LocalDateTime lastModifiedDate

) {

	static int MAX_KEYRESULT_COUNT = 3;
}
