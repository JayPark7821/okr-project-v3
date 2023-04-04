package kr.service.project.project.aggregate.team.domain;

import java.time.LocalDateTime;

import kr.service.model.project.team.ProjectRoleType;
import kr.service.project.project.domain.Project;
import kr.service.user.domain.User;

public record TeamMember(

	User user,
	Project project,
	ProjectRoleType projectRoleType,
	boolean isNew,
	boolean deleted,
	String createdBy,
	String lastModifiedBy,
	LocalDateTime createdDate,
	LocalDateTime lastModifiedDate

) {

}
