package kr.service.project.project.aggregate.initiative.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import kr.service.project.project.aggregate.feedback.domain.Feedback;
import kr.service.project.project.aggregate.keyresult.domain.KeyResult;
import kr.service.project.project.aggregate.team.domain.TeamMember;

public record Initiative(
	Long id,
	String initiativeToken,
	KeyResult keyResult,
	TeamMember teamMember,
	String name,
	LocalDate edt,
	LocalDate sdt,
	String detail,
	boolean done,
	List<Feedback> feedback,
	boolean deleted,
	String createdBy,
	String lastModifiedBy,
	LocalDateTime createdDate,
	LocalDateTime lastModifiedDate

) {
}
