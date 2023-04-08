package kr.service.okr.feedback.domain;

import kr.service.okr.initiative.domain.Initiative;
import kr.service.okr.model.project.feedback.FeedbackType;
import kr.service.okr.team.domain.TeamMember;
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

	public Feedback(
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

	@Builder
	private Feedback(
		final Long id,
		final String feedbackToken,
		final Initiative initiative,
		final TeamMember teamMember,
		final FeedbackType grade,
		final String opinion,
		final boolean checked
	) {
		this.id = id;
		this.feedbackToken = feedbackToken;
		this.initiative = initiative;
		this.teamMember = teamMember;
		this.grade = grade;
		this.opinion = opinion;
		this.checked = checked;
	}
}