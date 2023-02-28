package kr.jay.okrver3.interfaces.project;

import java.util.List;

public record ProjectMasterSaveDto(String name, String sdt, String edt, String objective, List<String> keyResults,
								   List<String> teamMembers) {
}
