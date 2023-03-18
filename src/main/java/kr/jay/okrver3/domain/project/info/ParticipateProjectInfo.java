package kr.jay.okrver3.domain.project.info;

import kr.jay.okrver3.domain.user.RoleType;

public record ParticipateProjectInfo(String projectToken, String projectName, RoleType roleType, String nextLeaderEmail) {
}
