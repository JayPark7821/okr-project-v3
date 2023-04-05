package kr.service.okr.persistence.entity.project.aggregate.feedback;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import kr.service.okr.model.project.feedback.FeedbackType;
import kr.service.okr.persistence.config.BaseEntity;
import kr.service.okr.persistence.entity.project.aggregate.initiative.InitiativeJpaEntity;
import kr.service.okr.persistence.entity.project.aggregate.team.TeamMemberJpaEntity;
import kr.service.okr.project.aggregate.feedback.domain.Feedback;
import kr.service.okr.util.TokenGenerator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@SQLDelete(sql = "UPDATE feedback SET deleted = true WHERE feedback_id = ?")
@Where(clause = "deleted = false")
@Table(name = "feedback")
public class FeedbackJpaEntity extends BaseEntity {

	private static final String FEEDBACK_PREFIX = "feedback-";

	@Id
	@Column(name = "feedback_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String feedbackToken;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "initiative_id", updatable = false)
	private InitiativeJpaEntity initiative;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumns(value = {
		@JoinColumn(name = "user_seq", referencedColumnName = "user_seq", updatable = false),
		@JoinColumn(name = "project_id", referencedColumnName = "project_id", updatable = false)
	}, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private TeamMemberJpaEntity teamMember;

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
	public FeedbackJpaEntity(InitiativeJpaEntity initiative, TeamMemberJpaEntity teamMember, FeedbackType grade,
		String opinion) {
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

	public Feedback toDomain() {
		return new Feedback(
			this.id,
			this.feedbackToken,
			this.initiative.toDomain(),
			this.teamMember.toDomain(),
			this.grade,
			this.opinion,
			this.isChecked
		);
	}
}


