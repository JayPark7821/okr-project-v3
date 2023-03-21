package kr.service.okr.interfaces.project.request;

import javax.validation.constraints.NotNull;

public record TeamMemberInviteRequest(
	@NotNull(message = "프로젝트 token은 필수 값입니다.")
	String projectToken,

	@NotNull(message = "email은 필수 값입니다.")
	String email) {
}
