package kr.jay.okrver3.domain.project.aggregate.feedback;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import kr.jay.okrver3.common.audit.BaseEntity;
import kr.jay.okrver3.common.utils.TokenGenerator;
import kr.jay.okrver3.domain.project.aggregate.initiative.Initiative;
import kr.jay.okrver3.domain.project.aggregate.team.TeamMember;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Feedback extends BaseEntity {

	private static final String FEEDBACK_PREFIX = "feedback-";

	@Id
	@Column(name = "feedback_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long feedbackId;

	private String feedbackToken;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "initiative_id", updatable = false)
	private Initiative initiative;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumns(value = {
		@JoinColumn(name = "user_seq", referencedColumnName = "user_seq", updatable = false),
		@JoinColumn(name = "project_id", referencedColumnName = "project_id", updatable = false)
	}, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private TeamMember teamMember;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "grade_mark")
	private FeedbackType grade;

	@NotNull
	@Column(name = "opinion")
	private String opinion;

	@NotNull
	@Column(name = "checked")
	private boolean isChecked;

	@Builder
	public Feedback(Initiative initiative, TeamMember teamMember, FeedbackType grade, String opinion) {
		this.feedbackToken = TokenGenerator.randomCharacterWithPrefix(FEEDBACK_PREFIX);
		this.initiative = initiative;
		this.teamMember = teamMember;
		this.grade = grade;
		this.opinion = opinion;
		this.isChecked = false;
	}

	public void checkFeedback() {
		this.isChecked = true;
	}

}


