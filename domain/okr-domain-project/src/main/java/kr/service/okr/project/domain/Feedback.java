package kr.service.okr.project.domain;

import kr.service.okr.project.domain.enums.FeedbackType;
import kr.service.okr.util.TokenGenerator;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Feedback {

	private static final String FEEDBACK_TOKEN_PREFIX = "feedback-";
	private Long id;
	private String feedbackToken;
	private Long initiativeId;
	private TeamMember teamMember;
	private FeedbackType grade;
	private String opinion;
	private boolean checked = Boolean.FALSE;

	public Feedback(
		final Long initiativeId,
		final TeamMember teamMember,
		final FeedbackType grade,
		final String opinion
	) {
		this.feedbackToken = TokenGenerator.randomCharacterWithPrefix(FEEDBACK_TOKEN_PREFIX);
		this.initiativeId = initiativeId;
		this.teamMember = teamMember;
		this.grade = grade;
		this.opinion = opinion;
	}

	@Builder
	private Feedback(
		final Long id,
		final String feedbackToken,
		final Long initiativeId,
		final TeamMember teamMember,
		final FeedbackType grade,
		final String opinion,
		final boolean checked
	) {
		this.id = id;
		this.feedbackToken = feedbackToken;
		this.initiativeId = initiativeId;
		this.teamMember = teamMember;
		this.grade = grade;
		this.opinion = opinion;
		this.checked = checked;
	}
}