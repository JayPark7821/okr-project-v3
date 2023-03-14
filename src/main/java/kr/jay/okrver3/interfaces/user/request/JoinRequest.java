package kr.jay.okrver3.interfaces.user.request;

public record JoinRequest(String guestUserId, String name, String email, String jobField) {
}
