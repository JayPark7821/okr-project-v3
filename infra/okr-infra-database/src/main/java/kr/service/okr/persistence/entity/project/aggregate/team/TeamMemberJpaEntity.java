package kr.service.okr.persistence.entity.project.aggregate.team;

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
import kr.service.okr.model.project.team.ProjectRoleType;
import kr.service.okr.persistence.config.BaseEntity;
import kr.service.okr.persistence.entity.project.ProjectJpaEntity;
import kr.service.okr.persistence.entity.user.UserJpaEntity;
import kr.service.okr.project.aggregate.team.domain.TeamMember;
import lombok.AccessLevel;
import lombok.Builder;
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
	@ManyToOne(targetEntity = UserJpaEntity.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_seq", updatable = false, insertable = false)
	private UserJpaEntity user;

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

	@Builder
	public TeamMemberJpaEntity(Long userSeq, ProjectJpaEntity project, ProjectRoleType projectRoleType, boolean isNew) {
		this.userSeq = userSeq;
		this.project = project;
		this.projectRoleType = projectRoleType;
		this.isNew = isNew;
	}

	public TeamMember toDomain() {
		return TeamMember.builder()
			.projectId(this.project.getId())
			.userSeq(this.userSeq)
			.projectRoleType(this.projectRoleType)
			.isNew(this.isNew)
			.build();
	}
}
