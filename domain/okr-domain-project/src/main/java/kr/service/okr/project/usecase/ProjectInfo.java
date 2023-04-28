package kr.service.okr.project.usecase;

import java.time.format.DateTimeFormatter;
import java.util.List;

import kr.service.okr.exception.ErrorCode;
import kr.service.okr.exception.OkrApplicationException;
import kr.service.okr.project.domain.Project;

public record ProjectInfo(
	String projectToken,
	String objective,
	String startDate,
	String endDate,
	String projectType,
	List<KeyResultInfo> keyResultInfos,
	int teamMembersCount,
	String roleType
) {

	public ProjectInfo(Project project, Long requesterSeq) {
		this(project.getProjectToken(),
			project.getObjective(),
			project.getStartDate().format(DateTimeFormatter.ISO_DATE),
			project.getEndDate().format(DateTimeFormatter.ISO_DATE),
			project.getType().name(),
			project.getKeyResults().stream().map(KeyResultInfo::new).toList(),
			project.getTeamMember().size(),
			project.getTeamMember()
				.stream()
				.filter(teamMember -> teamMember.getUserSeq().equals(requesterSeq))
				.findFirst()
				.orElseThrow(() -> new OkrApplicationException(ErrorCode.INTERNAL_SERVER_ERROR))
				.getProjectRoleType().name()
		);
	}
}

