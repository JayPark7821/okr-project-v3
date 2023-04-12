package kr.service.okr.persistence.entity.project.team;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.service.okr.persistence.config.BaseEntity;
import kr.service.okr.persistence.entity.project.ProjectJpaEntity;
import kr.service.okr.project.domain.Project;
import kr.service.okr.project.domain.TeamMember;
import kr.service.okr.project.domain.enums.ProjectRoleType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(TeamMemberId.class)
@Getter
@Entity
@SQLDelete(sql = "UPDATE team_member SET deleted = true WHERE user_seq = ? AND project_id = ?")
@Where(clause = "deleted = false")
public class TeamMemberJpaEntity extends BaseEntity {
	@Id
	@Column(name = "user_seq")
	private Long userSeq;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	private ProjectJpaEntity project;

	@Column(name = "project_role_type")
	@Enumerated(EnumType.STRING)
	private ProjectRoleType projectRoleType;

	@Column(name = "is_new")
	private boolean isNew;

	@Column(nullable = false)
	private boolean deleted = Boolean.FALSE;

	public TeamMemberJpaEntity(
		final Long userSeq,
		final ProjectJpaEntity project,
		final ProjectRoleType projectRoleType,
		final boolean isNew
	) {
		this.userSeq = userSeq;
		this.project = project;
		this.projectRoleType = projectRoleType;
		this.isNew = isNew;
	}

	public TeamMemberJpaEntity(TeamMember teamMember, ProjectJpaEntity project) {
		this(
			teamMember.getUserSeq(),
			project,
			teamMember.getProjectRoleType(),
			teamMember.isNew())
		;
	}

	public TeamMember toDomain(Project project) {
		final TeamMember teamMember = TeamMember.builder()
			.project(project)
			.userSeq(this.userSeq)
			.projectRoleType(this.projectRoleType)
			.isNew(this.isNew)
			.build();
		project.addTeamMember(teamMember);
		return teamMember;
	}
}
