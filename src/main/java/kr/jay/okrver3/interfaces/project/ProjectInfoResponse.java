package kr.jay.okrver3.interfaces.project;

public record ProjectInfoResponse(String projectToken, String name, String objective, String startDate, String endDate,
								  String projectType) {
}
