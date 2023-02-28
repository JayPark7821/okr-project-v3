package kr.jay.okrver3.domain.project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import kr.jay.okrver3.common.audit.BaseEntity;
import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.common.utils.TokenGenerator;
import kr.jay.okrver3.domain.keyresult.KeyResult;
import kr.jay.okrver3.domain.team.ProjectRoleType;
import kr.jay.okrver3.domain.team.TeamMember;
import kr.jay.okrver3.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "project", indexes = @Index(name = "idx_project_token", columnList = "projectToken"))
public class Project extends BaseEntity {

	private static final String PROJECT_MASTER_PREFIX = "project-";
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

	@Builder
	public Project(LocalDate startDate, LocalDate endDate, String objective,
		double progress, List<String> keyResultList) {
		this.projectToken = TokenGenerator.randomCharacterWithPrefix(PROJECT_MASTER_PREFIX);
		this.startDate = startDate;
		this.endDate = endDate;
		this.type = ProjectType.SINGLE;
		this.objective = objective;
		this.progress = progress;

		keyResultList.forEach(keyResult -> {
			this.keyResults.add(KeyResult.builder()
				.project(this)
				.name(keyResult)
				.index(this.keyResults.size() + 1)
				.build());
		});
	}

	public void addLeader(User leader) {
		this.teamMember.add(
			TeamMember.builder()
				.user(leader)
				.project(this)
				.projectRoleType(ProjectRoleType.LEADER)
				.isNew(true)
				.build()
		);
	}

	public void addTeamMember(User user) {
		this.teamMember.add(
			TeamMember.builder()
				.user(user)
				.project(this)
				.projectRoleType(ProjectRoleType.MEMBER)
				.isNew(true)
				.build());

		this.type = ProjectType.TEAM;
	}

	public void validateEmail(String email) {
		if (this.teamMember
			.stream()
			.anyMatch(tm -> tm.getUser().getEmail().equals(email)))
			throw new OkrApplicationException(ErrorCode.USER_ALREADY_PROJECT_MEMBER);

	}

	public TeamMember getProjectLeader() {
		return this.teamMember.stream().filter(tm -> tm.getProjectRoleType() == ProjectRoleType.LEADER)
			.findFirst()
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.PROJECT_LEADER_NOT_FOUND));
	}
}
