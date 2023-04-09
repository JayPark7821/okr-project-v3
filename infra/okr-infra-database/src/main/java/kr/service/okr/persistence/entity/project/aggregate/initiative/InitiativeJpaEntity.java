package kr.service.okr.persistence.entity.project.aggregate.initiative;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.service.okr.persistence.config.BaseEntity;
import kr.service.okr.persistence.entity.project.aggregate.feedback.FeedbackJpaEntity;
import kr.service.okr.persistence.entity.project.aggregate.keyresult.KeyResultJpaEntity;
import kr.service.okr.persistence.entity.project.aggregate.team.TeamMemberJpaEntity;
import kr.service.okr.project.domain.Initiative;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@SQLDelete(sql = "UPDATE initiative SET deleted = true WHERE initiative_id = ?")
@Where(clause = "deleted = false")
public class InitiativeJpaEntity extends BaseEntity {

	private static final String PROJECT_INITIATIVE_PREFIX = "initiative-";

	@Id
	@Column(name = "initiative_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String initiativeToken;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "key_result_id", updatable = false)
	private KeyResultJpaEntity keyResult;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumns(value = {
		@JoinColumn(name = "user_seq", referencedColumnName = "user_seq", updatable = false),
		@JoinColumn(name = "project_id", referencedColumnName = "project_id", updatable = false)
	}, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private TeamMemberJpaEntity teamMember;

	@Column(name = "initiative_name")
	@NotNull
	@Size(max = 50)
	private String name;

	@NotNull
	@Column(name = "initiative_edt")
	private LocalDate endDate;

	@NotNull
	@Column(name = "initiative_sdt")
	private LocalDate startDate;

	@Column(name = "initiative_detail")
	@NotNull
	@Size(max = 200)
	private String detail;

	@Column(name = "initiative_done")
	@NotNull
	private boolean done;

	@OneToMany(mappedBy = "initiative")
	private List<FeedbackJpaEntity> feedback = new ArrayList<>();

	@Column(nullable = false)
	private boolean deleted = Boolean.FALSE;

	public InitiativeJpaEntity(Initiative initiative) {
		this.id = initiative.getId();
		this.initiativeToken = initiative.getInitiativeToken();
		this.keyResult = new KeyResultJpaEntity(initiative.getKeyResult());
		// this.teamMember = new TeamMemberJpaEntity(initiative.getTeamMember());
		this.name = initiative.getName();
		this.endDate = initiative.getEndDate();
		this.startDate = initiative.getStartDate();
		this.detail = initiative.getDetail();
		this.done = initiative.isDone();
		this.feedback = initiative.getFeedback().stream().map(FeedbackJpaEntity::new).toList();
	}

	public void setKeyResult(KeyResultJpaEntity keyResult) {
		this.keyResult = keyResult;
	}

	public void done() {
		this.done = true;
	}

	public Initiative toDomain() {
		return Initiative.builder()
			.id(this.id)
			.initiativeToken(this.initiativeToken)
			.keyResult(this.keyResult.toDomain())
			// .teamMember(this.teamMember.toDomain())
			.name(this.name)
			.startDate(this.startDate)
			.endDate(this.endDate)
			.detail(this.detail)
			.done(this.done)
			.feedback(this.feedback.stream().map(FeedbackJpaEntity::toDomain).toList())
			.build();

	}
}


