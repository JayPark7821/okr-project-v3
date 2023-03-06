package kr.jay.okrver3.interfaces.feedback;

public record FeedbackSaveCommand(String opinion, String grade, String projectToken, String initiativeToken) {
}
