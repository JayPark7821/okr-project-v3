package kr.jay.okrver3.domain.project.service.info;

import java.util.List;

import kr.jay.okrver3.domain.user.User;

public record ProjectTeamMembersInfo(List<User> projectTeamMemberUsers, String projectName) {
}
