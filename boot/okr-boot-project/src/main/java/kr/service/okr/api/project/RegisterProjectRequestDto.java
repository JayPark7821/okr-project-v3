package kr.service.okr.api.project;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterProjectRequestDto(
	@NotNull(message = "목표를 작성해 주세요.")
	@Size(max = 20, message = "목표는 20자까지 작성할 수 있습니다.")
	String objective,

	// @DateValid(message = "8자리의 yyyy-MM-dd 형식이어야 합니다.", pattern = "yyyy-MM-dd")
	@NotNull(message = "목표 시작일을 선택해 주세요.")
	String startDate,

	// @DateValid(message = "8자리의 yyyy-MM-dd 형식이어야 합니다.", pattern = "yyyy-MM-dd")
	@NotNull(message = "목표 마감일을 선택해 주세요.")
	String endDate,

	List<String> teamMembers
) {
}
