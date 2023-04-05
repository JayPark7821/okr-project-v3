package kr.service.okr.persistence.entity.project;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kr.service.okr.model.project.ProjectType;
import kr.service.okr.persistence.config.BaseEntity;
import kr.service.okr.persistence.entity.project.aggregate.keyresult.KeyResultJpaEntity;
import kr.service.okr.persistence.entity.project.aggregate.team.TeamMemberJpaEntity;
import kr.service.okr.project.domain.Project;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@SQLDelete(sql = "UPDATE project SET deleted = true WHERE project_id = ?")
@Where(clause = "deleted = false")
@Table(name = "project", indexes = @Index(name = "idx_project_token", columnList = "projectToken"))
public class ProjectJpaEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "project_id")
	private Long id;

	private String projectToken;

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	private List<TeamMemberJpaEntity> teamMember;

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	private List<KeyResultJpaEntity> keyResults;

	@Column(name = "project_sdt")
	private LocalDate startDate;

	@Column(name = "project_edt")
	private LocalDate endDate;

	@Column(name = "project_type")
	@Enumerated(EnumType.STRING)
	private ProjectType type;

	@Column(name = "project_objective")
	@NotNull
	@Size(max = 50)
	private String objective;

	private double progress;

	@Column(nullable = false)
	private boolean deleted = Boolean.FALSE;

	@Builder
	public ProjectJpaEntity(Project project) {
		this.id = project.getId();
		this.keyResults = project.getKeyResults().stream().map(KeyResultJpaEntity::new).toList();
		this.teamMember = project.getTeamMember().stream().map(TeamMemberJpaEntity::new).toList();
		this.projectToken = project.getProjectToken();
		this.startDate = project.getStartDate();
		this.endDate = project.getEndDate();
		this.type = project.getType();
		this.objective = project.getObjective();
		this.progress = project.getProgress();
	}

	public void updateProgress(double progress) {
		this.progress = progress;
	}

	public Project toDomain() {
		return Project.builder()
			.id(this.id)
			.projectToken(this.projectToken)
			.teamMember(this.teamMember.stream().map(TeamMemberJpaEntity::toDomain).toList())
			.keyResults(this.keyResults.stream().map(KeyResultJpaEntity::toDomain).toList())
			.startDate(this.startDate)
			.endDate(this.endDate)
			.type(this.type)
			.objective(this.objective)
			.progress(this.progress)
			.deleted(this.deleted)
			.build();

	}

}
