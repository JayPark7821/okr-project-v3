package kr.service.okr.project.persistence.entity.project.initiative;

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
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.service.okr.config.BaseEntity;
import kr.service.okr.project.domain.Initiative;
import kr.service.okr.project.persistence.entity.project.feedback.FeedbackJpaEntity;
import kr.service.okr.project.persistence.entity.project.keyresult.KeyResultJpaEntity;
import kr.service.okr.project.persistence.entity.project.team.TeamMemberJpaEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@SQLDelete(sql = "UPDATE initiative SET deleted = true WHERE initiative_id = ?")
@Where(clause = "deleted = false")
@Table(name = "initiative")
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

	@Column(name = "key_result_id", insertable = false, updatable = false)
	private Long keyResultId;

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
	private boolean done = Boolean.FALSE;

	@OneToMany(mappedBy = "initiative")
	private List<FeedbackJpaEntity> feedback = new ArrayList<>();

	@Column(nullable = false)
	private boolean deleted = Boolean.FALSE;

	public InitiativeJpaEntity(Initiative initiative) {
		this.id = initiative.getId();
		this.initiativeToken = initiative.getInitiativeToken();
		this.keyResultId = initiative.getKeyResultId();
		this.teamMember = new TeamMemberJpaEntity(initiative.getTeamMember());
		this.name = initiative.getName();
		this.endDate = initiative.getEndDate();
		this.startDate = initiative.getStartDate();
		this.detail = initiative.getDetail();
		this.done = initiative.isDone();
		this.feedback = initiative.getFeedback().stream().map(FeedbackJpaEntity::new).toList();
	}

	public InitiativeJpaEntity(
		final String initiativeToken,
		final Long keyResultId,
		final TeamMemberJpaEntity teamMember,
		final String name,
		final String detail,
		final LocalDate endDate,
		final LocalDate startDate
	) {
		this.initiativeToken = initiativeToken;
		this.keyResultId = keyResultId;
		this.teamMember = teamMember;
		this.name = name;
		this.endDate = endDate;
		this.startDate = startDate;
		this.detail = detail;
	}

	public static InitiativeJpaEntity register(Initiative initiative) {
		return new InitiativeJpaEntity(
			initiative.getInitiativeToken(),
			initiative.getKeyResultId(),
			new TeamMemberJpaEntity(initiative.getTeamMember()),
			initiative.getName(),
			initiative.getDetail(),
			initiative.getEndDate(),
			initiative.getStartDate()
		);
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
			.keyResultId(keyResultId)
			.teamMember(this.teamMember.toDomain())
			.name(this.name)
			.startDate(this.startDate)
			.endDate(this.endDate)
			.detail(this.detail)
			.done(this.done)
			.feedback(this.feedback.stream().map(FeedbackJpaEntity::toDomain).toList())
			.build();
	}
}


