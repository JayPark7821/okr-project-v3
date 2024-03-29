package kr.service.okr.project.api;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterKeyResultRequest(
	@NotNull(message = "프로젝트 token은 필수 값입니다.")
	String projectToken,

	@NotNull(message = "Key Result는 필수 값입니다.")
	@Size(max = 50, message = "Key Result이름은 50자보다 클 수 없습니다.")
	String keyResultName
) {
}
