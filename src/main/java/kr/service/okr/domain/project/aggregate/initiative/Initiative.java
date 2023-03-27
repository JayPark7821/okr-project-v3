package kr.service.okr.domain.project.aggregate.initiative;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import kr.service.okr.common.audit.BaseEntity;
import kr.service.okr.common.utils.TokenGenerator;
import kr.service.okr.domain.project.Project;
import kr.service.okr.domain.project.aggregate.feedback.Feedback;
import kr.service.okr.domain.project.aggregate.keyresult.KeyResult;
import kr.service.okr.domain.project.aggregate.team.TeamMember;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@SQLDelete(sql = "UPDATE initiative SET deleted = true WHERE initiative_id = ?")
@Where(clause = "deleted = false")
public class Initiative extends BaseEntity {

	private static final String PROJECT_INITIATIVE_PREFIX = "initiative-";

	@Id
	@Column(name = "initiative_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long iniId;

	private String initiativeToken;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "key_result_id", updatable = false)
	private KeyResult keyResult;

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumns(value = {
		@JoinColumn(name = "user_seq", referencedColumnName = "user_seq", updatable = false),
		@JoinColumn(name = "project_id", referencedColumnName = "project_id", updatable = false)
	}, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private TeamMember teamMember;

	@Column(name = "initiative_name")
	@NotNull
	@Size(max = 50)
	private String name;

	@NotNull
	@Column(name = "initiative_edt")
	private LocalDate edt;

	@NotNull
	@Column(name = "initiative_sdt")
	private LocalDate sdt;

	@Column(name = "initiative_detail")
	@NotNull
	@Size(max = 200)
	private String detail;

	@Column(name = "initiative_done")
	@NotNull
	private boolean done;

	@OneToMany(mappedBy = "initiative")
	private List<Feedback> feedback = new ArrayList<>();

	@Column(nullable = false)
	private boolean deleted = Boolean.FALSE;

	@Builder
	public Initiative(TeamMember teamMember, String name, LocalDate edt, LocalDate sdt,
		String detail) {
		this.initiativeToken = TokenGenerator.randomCharacterWithPrefix(PROJECT_INITIATIVE_PREFIX);
		this.teamMember = teamMember;
		this.name = name;
		this.edt = edt;
		this.sdt = sdt;
		this.detail = detail;
		this.done = false;
	}

	public void setKeyResult(KeyResult keyResult) {
		this.keyResult = keyResult;
	}

	public void done() {
		this.done = true;
	}

	public Project getProject() {
		return this.keyResult.getProject();
	}

}


