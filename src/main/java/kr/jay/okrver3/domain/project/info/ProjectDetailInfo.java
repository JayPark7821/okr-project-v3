package kr.jay.okrver3.domain.project.info;

import java.time.LocalDate;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import kr.jay.okrver3.domain.project.Project;
import kr.jay.okrver3.domain.project.aggregate.team.TeamMember;

public record ProjectDetailInfo(String projectToken, boolean newProject, double progress, LocalDate sdt, LocalDate edt,
								int teamMemberCount, String projectType) {

	public ProjectDetailInfo(Project project, Long userSeq) {
		this(
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
