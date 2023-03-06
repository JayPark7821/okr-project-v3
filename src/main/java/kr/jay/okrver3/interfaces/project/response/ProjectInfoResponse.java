package kr.jay.okrver3.interfaces.project.response;

public record ProjectInfoResponse(String projectToken, String objective, String startDate, String endDate,
								  String projectType) {
}
