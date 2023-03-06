package kr.jay.okrver3.interfaces.project.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import kr.jay.okrver3.common.utils.DateValid;

public record ProjectInitiativeSaveRequest(
	@NotNull(message = "KR token은 필수 값입니다.")
	String keyResultToken,

	@NotNull(message = "행동 전략을 작성해 주세요.")
	@Size(max = 50, message = "행동 전략은 50자까지 작성할 수 있습니다.")
	String name,

	@DateValid(message = "8자리의 yyyy-MM-dd 형식이어야 합니다.", pattern = "yyyy-MM-dd")
	@NotNull(message = "행동 전략 마감일을 선택해 주세요.")
	String edt,

	@DateValid(message = "8자리의 yyyy-MM-dd 형식이어야 합니다.", pattern = "yyyy-MM-dd")
	@NotNull(message = "행동 전략 시작일을 선택해 주세요.")
	String sdt,

	@NotNull(message = "행동 전략의 상세 내용을 입력해 주세요.")
	@Size(max = 200, message = "상세 내용은 200자까지 작성할 수 있습니다.")
	String detail

) {

}
