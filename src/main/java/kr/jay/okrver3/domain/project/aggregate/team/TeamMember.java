package kr.jay.okrver3.domain.project.aggregate.team;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kr.jay.okrver3.common.audit.BaseEntity;
import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(TeamMemberId.class)
@Getter
@Entity
public class TeamMember extends BaseEntity {
	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_seq")
	private User user;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id")
	private Project project;

	@Column(name = "project_role_type")
	@Enumerated(EnumType.STRING)
	private ProjectRoleType projectRoleType;

	@Column(name = "is_new")
	private boolean isNew;

	@Builder
	public TeamMember(User user, Project project, ProjectRoleType projectRoleType, boolean isNew) {
		this.user = user;
		this.project = project;
		this.projectRoleType = projectRoleType;
		this.isNew = isNew;
	}
}
