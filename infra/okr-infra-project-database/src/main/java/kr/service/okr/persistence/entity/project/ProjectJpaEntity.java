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
import kr.service.okr.persistence.config.BaseEntity;
import kr.service.okr.persistence.entity.project.keyresult.KeyResultJpaEntity;
import kr.service.okr.persistence.entity.project.team.TeamMemberJpaEntity;
import kr.service.okr.project.domain.Project;
import kr.service.okr.project.domain.enums.ProjectType;
import lombok.AccessLevel;
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

	public ProjectJpaEntity(final Long id, final String projectToken, final List<TeamMemberJpaEntity> teamMember,
		final List<KeyResultJpaEntity> keyResults, final LocalDate startDate, final LocalDate endDate,
		final ProjectType type, final String objective,
		final double progress) {
		this.id = id;
		this.projectToken = projectToken;
		this.teamMember = teamMember;
		this.keyResults = keyResults;
		this.startDate = startDate;
		this.endDate = endDate;
		this.type = type;
		this.objective = objective;
		this.progress = progress;
	}

	public ProjectJpaEntity(Project project) {

		this.id = project.getId();
		this.projectToken = project.getProjectToken();
		this.teamMember = project.getTeamMember().stream()
			.map(teamMember -> new TeamMemberJpaEntity(teamMember, this)).toList();
		this.keyResults = project.getKeyResults().stream()
			.map(KeyResultJpaEntity::new).toList();
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
		final Project project = Project.builder()
			.id(this.id)
			.projectToken(this.projectToken)
			.keyResults(this.keyResults.stream().map(KeyResultJpaEntity::toDomain).toList())
			.startDate(this.startDate)
			.endDate(this.endDate)
			.type(this.type)
			.objective(this.objective)
			.progress(this.progress)
			.build();

		this.getTeamMember().forEach(teamMember -> teamMember.toDomain(project));
		return project;

	}

}
