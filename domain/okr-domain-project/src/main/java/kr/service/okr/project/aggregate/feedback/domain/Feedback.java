package kr.service.okr.project.aggregate.feedback.domain;

import kr.service.okr.model.project.feedback.FeedbackType;
import kr.service.okr.project.aggregate.initiative.domain.Initiative;
import kr.service.okr.project.aggregate.team.domain.TeamMember;
import kr.service.okr.util.TokenGenerator;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Feedback {

	private static final String FEEDBACK_TOKEN_PREFIX = "feedback-";
	private Long id;
	private String feedbackToken;
	private Initiative initiative;
	private TeamMember teamMember;
	private FeedbackType grade;
	private String opinion;
	private boolean checked = Boolean.FALSE;

	@Builder
	private Feedback(
		final Initiative initiative,
		final TeamMember teamMember,
		final FeedbackType grade,
		final String opinion
	) {
		this.feedbackToken = TokenGenerator.randomCharacterWithPrefix(FEEDBACK_TOKEN_PREFIX);
		this.initiative = initiative;
		this.teamMember = teamMember;
		this.grade = grade;
		this.opinion = opinion;
	}
}