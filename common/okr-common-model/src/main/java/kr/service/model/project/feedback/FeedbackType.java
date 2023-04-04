package kr.service.model.project.feedback;

public enum FeedbackType {

	GOOD_IDEA("GOOD_IDEA"),
	BEST_RESULT("BEST_RESULT"),
	BURNING_PASSION("BURNING_PASSION"),
	COMMUNI_KING("COMMUNI_KING"),
	;

	private final String code;

	public String getCode() {
		return code;
	}

	FeedbackType(final String code) {
		this.code = code;
	}
}
