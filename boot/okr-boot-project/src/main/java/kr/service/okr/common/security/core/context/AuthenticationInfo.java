package kr.service.okr.common.security.core.context;

import kr.service.okr.user.domain.User;

public record AuthenticationInfo(User user) {
}
