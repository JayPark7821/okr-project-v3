package kr.jay.okrver3.interfaces.user;

public record JoinRequestDto(String guestUserId, String name, String email, String jobField) {
}
