package kr.service.okr.project.persistence.entity.project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import kr.service.okr.config.BaseEntity;
import kr.service.okr.project.domain.Project;
import kr.service.okr.project.domain.TeamMember;
import kr.service.okr.project.domain.enums.ProjectType;
import kr.service.okr.project.persistence.entity.project.keyresult.KeyResultJpaEntity;
import kr.service.okr.project.persistence.entity.project.team.TeamMemberJpaEntity;
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
	private List<TeamMemberJpaEntity> teamMember = new ArrayList<>();

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	private List<KeyResultJpaEntity> keyResults = new ArrayList<>();

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

	private double progress = 0.0D;

	@Column(nullable = false)
	private boolean deleted = Boolean.FALSE;

	private boolean completed = Boolean.FALSE;

	public ProjectJpaEntity(
		final String projectToken,
		final LocalDate startDate,
		final LocalDate endDate,
		final String objective
	) {
		this.projectToken = projectToken;
		this.startDate = startDate;
		this.endDate = endDate;
		this.objective = objective;
	}

	public static ProjectJpaEntity createFrom(Project project) {
		return new ProjectJpaEntity(project.getProjectToken(), project.getStartDate(), project.getEndDate(),
			project.getObjective());
	}

	public void addTeamMember(TeamMember teamMember) {
		this.teamMember.add(TeamMemberJpaEntity.createFrom(teamMember, this));
	}

	public ProjectJpaEntity(Project project) {

		this.id = project.getId();
		this.projectToken = project.getProjectToken();
		this.teamMember = project.getTeamMember().stream()
			.map(TeamMemberJpaEntity::new).toList();
		this.keyResults = project.getKeyResults().stream()
			.map(KeyResultJpaEntity::new).toList();
		this.startDate = project.getStartDate();
		this.endDate = project.getEndDate();
		this.type = project.getType();
		this.objective = project.getObjective();
		this.progress = project.getProgress();
		this.completed = project.isCompleted();
		this.deleted = project.isDeleted();

	}

	public void updateProgress(double progress) {
		this.progress = progress;
	}

	public Project toDomain() {

		return Project.builder()
			.id(this.id)
			.projectToken(this.projectToken)
			.teamMember(this.teamMember.stream().map(TeamMemberJpaEntity::toDomain).collect(Collectors.toList()))
			.keyResults(this.keyResults.stream().map(KeyResultJpaEntity::toDomain).collect(Collectors.toList()))
			.startDate(this.startDate)
			.endDate(this.endDate)
			.type(this.type)
			.objective(this.objective)
			.progress(this.progress)
			.completed(this.completed)
			.build();

	}

}
