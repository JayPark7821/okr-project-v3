package kr.jay.okrver3.interfaces.project.request;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import kr.jay.okrver3.common.utils.DateValid;

public record ProjectSaveRequest(

	@NotNull(message = "목표를 작성해 주세요.")
	@Size(max = 20, message = "목표는 20자까지 작성할 수 있습니다.")
	String objective,

	@DateValid(message = "8자리의 yyyy-MM-dd 형식이어야 합니다.", pattern = "yyyy-MM-dd")
	@NotNull(message = "목표 시작일을 선택해 주세요.")
	String sdt,

	@DateValid(message = "8자리의 yyyy-MM-dd 형식이어야 합니다.", pattern = "yyyy-MM-dd")
	@NotNull(message = "목표 마감일을 선택해 주세요.")
	String edt,

	@NotNull(message = "핵심 결과를 작성해 주세요.")
	@Size(min = 1, max = 3, message = "핵심 결과는 1~3개 까지만 등록이 가능합니다.")
	List<String> keyResults,

	List<String> teamMembers) {
}
