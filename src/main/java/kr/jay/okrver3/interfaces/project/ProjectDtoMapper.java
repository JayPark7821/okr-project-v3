package kr.jay.okrver3.interfaces.project;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import kr.jay.okrver3.domain.project.ProjectType;
import kr.jay.okrver3.domain.project.SortType;
import kr.jay.okrver3.domain.project.command.FeedbackSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectDetailRetrieveCommand;
import kr.jay.okrver3.domain.project.command.ProjectInitiativeSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectKeyResultSaveCommand;
import kr.jay.okrver3.domain.project.command.ProjectSaveCommand;
import kr.jay.okrver3.domain.project.command.TeamMemberInviteCommand;
import kr.jay.okrver3.domain.project.info.InitiativeDetailInfo;
import kr.jay.okrver3.domain.project.info.InitiativeInfo;
import kr.jay.okrver3.domain.project.info.ProjectDetailInfo;
import kr.jay.okrver3.domain.project.info.ProjectInfo;
import kr.jay.okrver3.domain.project.info.ProjectSideMenuInfo;
import kr.jay.okrver3.domain.project.info.TeamMemberUserInfo;
import kr.jay.okrver3.interfaces.project.request.FeedbackSaveRequest;
import kr.jay.okrver3.interfaces.project.request.ProjectInitiativeSaveRequest;
import kr.jay.okrver3.interfaces.project.request.ProjectKeyResultSaveRequest;
import kr.jay.okrver3.interfaces.project.request.ProjectSaveRequest;
import kr.jay.okrver3.interfaces.project.request.TeamMemberInviteRequest;
import kr.jay.okrver3.interfaces.project.response.InitiativeDetailResponse;
import kr.jay.okrver3.interfaces.project.response.ProjectDetailResponse;
import kr.jay.okrver3.interfaces.project.response.ProjectInfoResponse;
import kr.jay.okrver3.interfaces.project.response.ProjectInitiativeResponse;
import kr.jay.okrver3.interfaces.project.response.ProjectSideMenuResponse;
import kr.jay.okrver3.interfaces.project.response.ProjectTeamMemberResponse;

@Component
public class ProjectDtoMapper {

	ProjectSaveCommand of(ProjectSaveRequest dto) {
		return new ProjectSaveCommand(
			dto.objective(),
			dto.sdt(),
			dto.edt(),
			dto.keyResults(),
			dto.teamMembers()
		);
	}

	ProjectInfoResponse of(ProjectInfo info) {
		return new ProjectInfoResponse(
			info.projectToken(),
			info.objective(),
			info.startDate(),
			info.endDate(),
			info.projectType()
		);
	}

	ProjectDetailRetrieveCommand of(SortType sortType, ProjectType projectType, String includeFinishedProjectYN,
		Pageable pageable) {
		return new ProjectDetailRetrieveCommand(sortType, projectType, includeFinishedProjectYN, pageable);
	}

	ProjectDetailResponse of(ProjectDetailInfo info) {
		return new ProjectDetailResponse(
			info.projectToken(),
			info.newProject(),
			info.progress(),
			info.sdt(),
			info.edt(),
			info.teamMemberCount(),
			info.projectType());
	}

	TeamMemberInviteCommand of(TeamMemberInviteRequest dto) {
		return new TeamMemberInviteCommand(dto.projectToken(), dto.email());
	}

	ProjectSideMenuResponse of(ProjectSideMenuInfo info) {
		return new ProjectSideMenuResponse(
			info.progress(),
			info.teamMembers().stream().map(this::of).toList()
		);
	}

	ProjectTeamMemberResponse of(TeamMemberUserInfo info) {
		return new ProjectTeamMemberResponse(
			info.userEmail(),
			info.userName(),
			info.profileImage(),
			info.jobField()
		);
	}

	ProjectKeyResultSaveCommand of(ProjectKeyResultSaveRequest dto) {
		return new ProjectKeyResultSaveCommand(
			dto.projectToken(),
			dto.keyResultName()
		);
	}

	ProjectInitiativeSaveCommand of(ProjectInitiativeSaveRequest dto) {
		return new ProjectInitiativeSaveCommand(
			dto.keyResultToken(),
			dto.name(),
			LocalDate.parse(dto.sdt(), DateTimeFormatter.ISO_DATE),
			LocalDate.parse(dto.edt(), DateTimeFormatter.ISO_DATE),
			dto.detail()
		);
	}

	ProjectInitiativeResponse of(InitiativeInfo info) {
		return new ProjectInitiativeResponse(
			info.initiativeToken(), info.initiativeName(), info.done(),
			this.of(info.user())
		);

	}

	public FeedbackSaveCommand of(FeedbackSaveRequest dto) {
		return new FeedbackSaveCommand(
			dto.opinion(),
			dto.grade(),
			dto.initiativeToken()
		);
	}

	public InitiativeDetailResponse of(InitiativeDetailInfo info) {
		return new InitiativeDetailResponse(
			info.done(),
			info.user(),
			info.startDate(),
			info.endDate(),
			info.initiativeToken(),
			info.initiativeName(),
			info.initiativeDetail(),
			info.myInitiative()
		);
	}

}

