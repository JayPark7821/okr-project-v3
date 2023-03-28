package kr.service.okr.domain.project.aggregate.team;

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
import kr.service.okr.common.audit.BaseEntity;
import kr.service.okr.domain.project.Project;
import kr.service.okr.domain.user.User;
import kr.service.okrcommon.common.exception.ErrorCode;
import kr.service.okrcommon.common.exception.OkrApplicationException;
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
public class TeamMember extends BaseEntity {
	@Id
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_seq", updatable = false, insertable = false)
	private User user;

	@Column(name = "user_seq")
	private Long userSeq;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	private Project project;

	@Column(name = "project_role_type")
	@Enumerated(EnumType.STRING)
	private ProjectRoleType projectRoleType;

	@Column(name = "is_new")
	private boolean isNew;

	@Column(nullable = false)
	private boolean deleted = Boolean.FALSE;

	@Builder
	public TeamMember(Long userSeq, Project project, ProjectRoleType projectRoleType, boolean isNew) {
		this.userSeq = userSeq;
		this.project = project;
		this.projectRoleType = projectRoleType;
		this.isNew = isNew;
	}

	public void makeMemberAsProjectLeader() {
		this.projectRoleType = ProjectRoleType.LEADER;
	}

	public void changeLeaderRoleToMember() {
		if (this.projectRoleType != ProjectRoleType.LEADER) {
			throw new OkrApplicationException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
		this.projectRoleType = ProjectRoleType.MEMBER;
	}
}
