package kr.jay.okrver3.interfaces.user.response;

import kr.jay.okrver3.domain.user.RoleType;

public record ParticipateProjectResponse(String projectToken, String projectName, RoleType roleType, String nextLeaderEmail) {
}
