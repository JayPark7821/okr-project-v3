package kr.service.okr.domain.project.aggregate.feedback;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import kr.service.okr.common.audit.BaseEntity;
import kr.service.okr.common.utils.TokenGenerator;
import kr.service.okr.domain.project.aggregate.initiative.Initiative;
import kr.service.okr.domain.project.aggregate.team.TeamMember;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@SQLDelete(sql = "UPDATE feedback SET deleted = true WHERE feedback_id = ?")
@Where(clause = "deleted = false")
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

	@Column(nullable = false)
	private boolean deleted = Boolean.FALSE;

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


