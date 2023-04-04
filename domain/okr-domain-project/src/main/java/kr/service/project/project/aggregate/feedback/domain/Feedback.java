package kr.service.project.project.aggregate.feedback.domain;

import java.time.LocalDateTime;

import kr.service.model.project.feedback.FeedbackType;
import kr.service.project.project.aggregate.initiative.domain.Initiative;
import kr.service.project.project.aggregate.team.domain.TeamMember;

public record Feedback(
	Long id,
	String feedbackToken,
	Initiative initiative,
	TeamMember teamMember,
	FeedbackType grade,
	String opinion,
	boolean checked,
	boolean deleted,
	String createdBy,
	String lastModifiedBy,
	LocalDateTime createdDate,
	LocalDateTime lastModifiedDate
) {
}
