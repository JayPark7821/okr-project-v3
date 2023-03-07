package kr.jay.okrver3.domain.project.aggregate.feedback;

import java.util.Arrays;

import kr.jay.okrver3.common.exception.ErrorCode;
import kr.jay.okrver3.common.exception.OkrApplicationException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FeedbackType {

	GOOD_IDEA("GOOD_IDEA"),
	BEST_RESULT("BEST_RESULT"),
	BURNING_PASSION("BURNING_PASSION"),
	COMMUNI_KING("COMMUNI_KING"),
	;

	private final String code;

	public static FeedbackType of(String code) {
		return Arrays.stream(FeedbackType.values())
			.filter(r -> r.getCode().equals(code))
			.findAny()
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_FEEDBACK_TYPE));
	}

}
