package kr.service.okr.domain.project.info;

import java.util.List;

public record InitiativeDoneInfo(List<Long> teamMemberUserSeqs, String initiativeToken, String initiativeName, String username) {
}
