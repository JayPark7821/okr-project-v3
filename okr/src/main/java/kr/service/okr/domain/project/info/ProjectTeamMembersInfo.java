package kr.service.okr.domain.project.info;

import java.util.List;

public record ProjectTeamMembersInfo(List<Long> teamMemberSeq, String projectName) {
}
