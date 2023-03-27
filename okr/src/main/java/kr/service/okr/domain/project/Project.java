package kr.service.okr.domain.project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import kr.service.okr.common.audit.BaseEntity;
import kr.service.okr.common.exception.ErrorCode;
import kr.service.okr.common.exception.OkrApplicationException;
import kr.service.okr.common.utils.TokenGenerator;
import kr.service.okr.domain.project.aggregate.keyresult.KeyResult;
import kr.service.okr.domain.project.aggregate.team.ProjectRoleType;
import kr.service.okr.domain.project.aggregate.team.TeamMember;
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
public class Project extends BaseEntity {

	private static final String PROJECT_MASTER_PREFIX = "project-";
	private static final int MAX_KEYRESULT_COUNT = 3;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "project_id")
	private Long id;

	private String projectToken;

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	private final List<TeamMember> teamMember = new ArrayList<>();

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	private final List<KeyResult> keyResults = new ArrayList<>();

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
	public Project(LocalDate startDate, LocalDate endDate, String objective,
		double progress) {
		this.projectToken = TokenGenerator.randomCharacterWithPrefix(PROJECT_MASTER_PREFIX);
		this.startDate = startDate;
		this.endDate = endDate;
		this.type = ProjectType.SINGLE;
		this.objective = objective;
		this.progress = progress;
	}

	public void addLeader(Long leaderUserSeq) {
		this.teamMember.add(
			TeamMember.builder()
				.userSeq(leaderUserSeq)
				.project(this)
				.projectRoleType(ProjectRoleType.LEADER)
				.isNew(true)
				.build()
		);
	}

	public void addTeamMember(Long userSeq) {
		this.teamMember.add(
			TeamMember.builder()
				.userSeq(userSeq)
				.project(this)
				.projectRoleType(ProjectRoleType.MEMBER)
				.isNew(true)
				.build());

		this.type = ProjectType.TEAM;
	}

	public void validateTeamMember(Long userSeq) {
		if (this.teamMember
			.stream()
			.anyMatch(tm -> tm.getUser().getUserSeq().equals(userSeq)))
			throw new OkrApplicationException(ErrorCode.USER_ALREADY_PROJECT_MEMBER);

	}

	public TeamMember getProjectLeader() {
		return this.teamMember.stream().filter(tm -> tm.getProjectRoleType() == ProjectRoleType.LEADER)
			.findFirst()
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.USER_IS_NOT_LEADER));
	}

	public boolean isValidUntilToday() {
		return this.endDate.isAfter(LocalDate.now()) &&
			(this.startDate.isEqual(LocalDate.now()) || this.startDate.isBefore(LocalDate.now()));
	}

	public boolean isKeyResultAddable() {
		return this.keyResults.size() < MAX_KEYRESULT_COUNT;
	}

	public String addKeyResult(String keyResultName) {
		KeyResult keyResult = KeyResult.builder()
			.project(this)
			.name(keyResultName)
			.index(this.keyResults.size() + 1)
			.build();

		this.keyResults.add(keyResult);

		return keyResult.getKeyResultToken();
	}

	public void updateProgress(double progress) {
		this.progress = progress;
	}

	public Optional<TeamMember> getNextProjectLeader() {
		return this.teamMember.stream()
			.filter(tm -> tm.getProjectRoleType() == ProjectRoleType.MEMBER)
			.min((tm1, tm2) -> tm1.getCreatedDate().compareTo(tm2.getCreatedDate()));
	}

	public void deleteProject() {
		this.deleted = true;
	}
}
