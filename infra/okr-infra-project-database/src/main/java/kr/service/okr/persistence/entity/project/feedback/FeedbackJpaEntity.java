package kr.service.okr.persistence.entity.project.feedback;

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
import kr.service.okr.persistence.config.BaseEntity;
import kr.service.okr.persistence.entity.project.initiative.InitiativeJpaEntity;
import kr.service.okr.persistence.entity.project.team.TeamMemberJpaEntity;
import kr.service.okr.project.domain.Feedback;
import kr.service.okr.project.domain.enums.FeedbackType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@SQLDelete(sql = "UPDATE feedback SET deleted = true WHERE feedback_id = ?")
@Where(clause = "deleted = false")
@Table(name = "feedback")
public class FeedbackJpaEntity extends BaseEntity {

	@Id
	@Column(name = "feedback_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String feedbackToken;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "initiative_id", updatable = false)
	private InitiativeJpaEntity initiative;

	@Column(name = "initiative_id", insertable = false, updatable = false)
	private Long initiativeId;

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
	private boolean checked;

	@Column(nullable = false)
	private boolean deleted = Boolean.FALSE;

	public FeedbackJpaEntity(final Feedback feedback) {
		this.id = feedback.getId();
		this.feedbackToken = feedback.getFeedbackToken();
		this.initiativeId = feedback.getInitiativeId();
		this.teamMember = new TeamMemberJpaEntity(feedback.getTeamMember());
		this.grade = feedback.getGrade();
		this.opinion = feedback.getOpinion();
		this.checked = feedback.isChecked();
	}

	public void checkFeedback() {
		this.checked = true;
	}

	public Feedback toDomain() {
		return Feedback.builder()
			.id(this.id)
			.feedbackToken(this.feedbackToken)
			.initiativeId(this.initiativeId)
			.teamMember(this.teamMember.toDomain())
			.grade(this.grade)
			.opinion(this.opinion)
			.checked(this.checked)
			.build();

	}
}


