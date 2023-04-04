package kr.service.persistence.entity.project;

import java.time.LocalDate;
import java.util.ArrayList;
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
import kr.service.model.project.ProjectType;
import kr.service.okrcommonutil.util.TokenGenerator;
import kr.service.persistence.config.BaseEntity;
import kr.service.persistence.entity.project.aggregate.keyresult.KeyResultJpaEntity;
import kr.service.persistence.entity.project.aggregate.team.TeamMemberJpaEntity;
import kr.service.project.project.domain.Project;
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

	private static final String PROJECT_MASTER_PREFIX = "project-";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "project_id")
	private Long id;

	private String projectToken;

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	private final List<TeamMemberJpaEntity> teamMember = new ArrayList<>();

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	private final List<KeyResultJpaEntity> keyResults = new ArrayList<>();

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
	public ProjectJpaEntity(LocalDate startDate, LocalDate endDate, String objective,
		double progress) {
		this.projectToken = TokenGenerator.randomCharacterWithPrefix(PROJECT_MASTER_PREFIX);
		this.startDate = startDate;
		this.endDate = endDate;
		this.type = ProjectType.SINGLE;
		this.objective = objective;
		this.progress = progress;
	}

	public void updateProgress(double progress) {
		this.progress = progress;
	}

	public Project toDomain() {
		return new Project(
			this.id,
			this.projectToken,
			this.teamMember,
			this.keyResults,
			this.startDate,
			this.endDate,
			this.type,
			this.objective,
			this.progress,
			this.deleted,
			this.getCreatedBy(),
			this.getLastModifiedBy(),
			this.getCreatedDate(),
			this.getLastModifiedDate()
		);
	}
}
