package kr.jay.okrver3.interfaces.project.response;

import java.util.List;

public record ProjectSideMenuResponse(String progress, List<ProjectTeamMemberResponse> teamMembers) {

}
