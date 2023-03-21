package kr.service.okr.interfaces.project.response;

import java.util.List;

public record ProjectSideMenuResponse(String progress, List<ProjectTeamMemberResponse> teamMembers) {

}
