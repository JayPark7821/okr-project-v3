package kr.service.project.project.aggregate.keyresult.domain;

import java.time.LocalDateTime;
import java.util.List;

import kr.service.project.project.aggregate.initiative.domain.Initiative;
import kr.service.project.project.domain.Project;

public record KeyResult(
	Long id,
	String keyResultToken,
	Project project,
	String name,
	Integer keyResultIndex,
	List<Initiative> initiative,
	boolean deleted,
	String createdBy,
	String lastModifiedBy,
	LocalDateTime createdDate,
	LocalDateTime lastModifiedDate

) {
}
