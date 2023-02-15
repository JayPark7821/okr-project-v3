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

import kr.jay.okrver3.common.utils.TokenGenerator;
import kr.jay.okrver3.domain.keyresult.KeyResult;
import kr.jay.okrver3.domain.team.TeamMember;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "project", indexes = @Index(name = "idx_project_token", columnList = "projectToken"))
public class Project {

	private static final String PROJECT_MASTER_PREFIX = "project-";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "project_id")
	private Long id;

	private String projectToken;

	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
	private List<TeamMember> teamMember = new ArrayList<>();

	@OneToMany(mappedBy = "project")
	private List<KeyResult> keyResults = new ArrayList<>();

	private String name;

	private LocalDate startDate;

	private LocalDate endDate;

	@Enumerated(EnumType.STRING)
	private ProjectType type;

	private String objective;

	private double progress;

	@Builder
	public Project(String name, LocalDate startDate, LocalDate endDate, ProjectType type, String objective,
		double progress) {
		this.projectToken = TokenGenerator.randomCharacterWithPrefix(PROJECT_MASTER_PREFIX);
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.type = type;
		this.objective = objective;
		this.progress = progress;
	}

	public void inviteTeamMember(TeamMember teamMember) {
		this.teamMember.add(teamMember);
	}
}
