package kr.jay.okrver3.domain.project.service;

import java.util.List;

import kr.jay.okrver3.domain.user.User;

public record ProjectTeamMemberInfo(List<User> projectTeamMemberUsers, String projectName) {
}
