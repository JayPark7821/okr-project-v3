package kr.jay.okrver3.interfaces.project;

import java.util.List;

public record ProjectMasterSaveDto(String objective, String sdt, String edt, List<String> keyResults,
								   List<String> teamMembers) {
}
