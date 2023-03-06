package kr.jay.okrver3.interfaces.feedback;

import org.springframework.stereotype.Component;

import kr.jay.okrver3.interfaces.feedback.request.FeedbackSaveRequest;

@Component
public class FeedbackDtoMapper {
	public FeedbackSaveCommand of(FeedbackSaveRequest dto) {
		return new FeedbackSaveCommand(
			dto.opinion(),
			dto.grade(),
			dto.projectToken(),
			dto.initiativeToken()
		);
	}
}
