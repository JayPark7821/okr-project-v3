package kr.jay.okrver3.interfaces.project.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record FeedbackSaveRequest(

	@NotNull(message = "피드백 메시지를 작성해 주세요.")
	@Size(max = 300, message = "피드백은 300자까지 작성할 수 있습니다.")
	String opinion,

	@NotNull(message = "피드백 이모티콘을 선택해주세요.")
	String grade,

	@NotNull(message = "프로젝트 token은 필수 값입니다.")
	String projectToken,

	@NotNull(message = "Initiative token은 필수 값입니다.")
	String initiativeToken
) {
}
