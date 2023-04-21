package kr.service.okr.project.persistence.entity.project.team;

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
import jakarta.persistence.Table;
import kr.service.okr.config.BaseEntity;
import kr.service.okr.project.domain.TeamMember;
import kr.service.okr.project.domain.enums.ProjectRoleType;
import kr.service.okr.project.persistence.entity.project.ProjectJpaEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(TeamMemberId.class)
@Getter
@Entity
@SQLDelete(sql = "UPDATE team_member SET deleted = true WHERE user_seq = ? AND project_id = ?")
@Where(clause = "deleted = false")
@Table(name = "team_member")
public class TeamMemberJpaEntity extends BaseEntity {
	@Id
	@Column(name = "user_seq")
	private Long userSeq;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	private ProjectJpaEntity project;

	@Column(name = "project_id", insertable = false, updatable = false)
	private Long projectId;

	@Column(name = "project_role_type")
	@Enumerated(EnumType.STRING)
	private ProjectRoleType projectRoleType;

	@Column(name = "is_new")
	private boolean isNew = Boolean.FALSE;

	@Column(nullable = false)
	private boolean deleted = Boolean.FALSE;

	public TeamMemberJpaEntity(
		final Long userSeq,
		final ProjectJpaEntity project,
		final ProjectRoleType projectRoleType
	) {
		this.userSeq = userSeq;
		this.project = project;
		this.projectRoleType = projectRoleType;
	}

	public static TeamMemberJpaEntity createFrom(final TeamMember teamMember, final ProjectJpaEntity project) {
		return new TeamMemberJpaEntity(
			teamMember.getUserSeq(),
			project,
			teamMember.getProjectRoleType()
		);
	}

	public TeamMemberJpaEntity(final TeamMember teamMember) {
		this.userSeq = teamMember.getUserSeq();
		this.projectId = teamMember.getProjectId();
		this.projectRoleType = teamMember.getProjectRoleType();
		this.isNew = teamMember.isNew();
	}

	public TeamMember toDomain() {
		return TeamMember.builder()
			.projectId(projectId)
			.userSeq(this.userSeq)
			.projectRoleType(this.projectRoleType)
			.isNew(this.isNew)
			.build();
	}
}
