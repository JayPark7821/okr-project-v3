package kr.service.okr.domain.project.info;

import java.time.LocalDate;

import kr.service.okr.common.exception.ErrorCode;
import kr.service.okr.common.exception.OkrApplicationException;
import kr.service.okr.domain.project.Project;
import kr.service.okr.domain.project.aggregate.team.TeamMember;

public record ProjectDetailInfo(String objective, String projectToken, boolean newProject, double progress,
								LocalDate sdt, LocalDate edt,
								int teamMemberCount, String projectType) {

	public ProjectDetailInfo(Project project, Long userSeq) {
		this(
			project.getObjective(),
			project.getProjectToken(),
			project.getTeamMember().stream()
				.filter(t -> t.getUser().getUserSeq().equals(userSeq))
				.findFirst()
				.map(TeamMember::isNew)
				.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_USER_INFO)),
			project.getProgress(),
			project.getStartDate(),
			project.getEndDate(),
			project.getTeamMember().size(),
			project.getType().name());
	}
}
