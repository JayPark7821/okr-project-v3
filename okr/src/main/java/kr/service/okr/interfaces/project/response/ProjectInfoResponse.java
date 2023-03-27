package kr.service.okr.interfaces.project.response;

import java.util.List;

public record ProjectInfoResponse(String projectToken, String objective, String startDate, String endDate,
								  String projectType, List<KeyResultResponse> keyResults, int teamMembersCount) {
}

